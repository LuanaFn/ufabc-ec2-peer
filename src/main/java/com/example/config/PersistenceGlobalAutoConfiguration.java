package com.example.config;

import javax.sql.DataSource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.example.repository.global", entityManagerFactoryRef = "globalEntityManager", transactionManagerRef = "globalTransactionManager")
public class PersistenceGlobalAutoConfiguration {
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource globalDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	// userEntityManager bean

	// userTransactionManager bean
}
