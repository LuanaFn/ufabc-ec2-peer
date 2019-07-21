package com.example.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.example.repository.local", entityManagerFactoryRef = "localEntityManager", transactionManagerRef = "localTransactionManager")
public class LocalBDConfig {
	@Autowired
	private Environment env;
	
	@Value("${spring.datasource.local.url}")
	private  String jdbcUrl;

	@Bean
	public LocalContainerEntityManagerFactoryBean localEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		
		HikariDataSource ds = (HikariDataSource) localDataSource();
		if(ds.getJdbcUrl() == null) ds.setJdbcUrl(jdbcUrl);
		
		em.setDataSource(ds);
		em.setPackagesToScan(new String[] { "com.example.dto.local" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.local.properties.hibernate.dialect"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Bean
	public DataSource localDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.local.driverClassName"));
		dataSource.setUrl(env.getProperty("spring.datasource.local.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.local.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.local.password"));

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager localTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(localEntityManager().getObject());
		return transactionManager;
	}
}
