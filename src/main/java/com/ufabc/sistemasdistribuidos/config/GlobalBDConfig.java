package com.ufabc.sistemasdistribuidos.config;

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
@EnableJpaRepositories(basePackages = "com.ufabc.sistemasdistribuidos.repository.global", entityManagerFactoryRef = "globalEntityManager", transactionManagerRef = "globalTransactionManager")
public class GlobalBDConfig {
	@Autowired
	private Environment env;

	@Value("${spring.datasource.driverClassName}")
	String driverClassName;

	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Value("${spring.datasource.hikari.connection-timeout}")
	private String conTimeout;
	
	@Value("${spring.datasource.hikari.minimum-idle}")
	private String minIdle;
	
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private String maxPool;
	
	@Value("${spring.datasource.hikari.idle-timeout}")
	private String idleTimeout;
	
	@Value("${spring.datasource.hikari.max-lifetime}")
	private String maxLifetime;
	
	@Value("${spring.datasource.hikari.auto-commit}")
	private String autoCommit;

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean globalEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		HikariDataSource ds = (HikariDataSource) globalDataSource();
		if (ds.getJdbcUrl() == null) ds.setJdbcUrl(jdbcUrl);
		if(ds.getMaximumPoolSize() > 2) ds.setMaximumPoolSize(Integer.valueOf(maxPool));

		em.setDataSource(ds);
		em.setPackagesToScan(new String[] { "com.ufabc.sistemasdistribuidos.dto.global" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
		
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Primary
	@Bean
	public DataSource globalDataSource() {

		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driverClassName);
		config.setJdbcUrl(jdbcUrl);
		config.setAutoCommit(Boolean.valueOf(autoCommit));
		config.setConnectionTimeout(Long.valueOf(conTimeout));
		config.setMinimumIdle(Integer.valueOf(minIdle));
		config.setMaximumPoolSize(Integer.valueOf(maxPool));
		config.setIdleTimeout(Long.valueOf(idleTimeout));
		config.setMaxLifetime(Long.valueOf(maxLifetime));
		
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
