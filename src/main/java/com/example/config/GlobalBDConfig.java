package com.example.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.example.repository.global", entityManagerFactoryRef = "globalEntityManager", transactionManagerRef = "globalTransactionManager")
public class GlobalBDConfig {
	@Autowired
	private Environment env;
	
	@Value("${spring.datasource.driverClassName}")
	String driverClassName;
	
	@Value("${spring.datasource.url}")
	private  String jdbcUrl;

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean globalEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		
		HikariDataSource ds = (HikariDataSource) globalDataSource();
		if(ds.getJdbcUrl() == null) ds.setJdbcUrl(jdbcUrl);
		
		em.setDataSource(ds);
		em.setPackagesToScan(new String[] { "com.example.dto.global" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Primary
	@Bean
	public DataSource globalDataSource() {

		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driverClassName);
		config.setJdbcUrl(jdbcUrl);
		// config.setUsername(env.getProperty("spring.datasource.username"));
		// config.setPassword(env.getProperty("spring.datasource.password"));

		return new HikariDataSource(config);
	}

	@Primary
	@Bean
	public PlatformTransactionManager globalTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(globalEntityManager().getObject());
		return transactionManager;
	}
}
