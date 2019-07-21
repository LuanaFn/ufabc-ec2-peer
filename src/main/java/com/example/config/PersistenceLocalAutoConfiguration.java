package com.example.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "com.example.repository.local", entityManagerFactoryRef = "localEntityManager", transactionManagerRef = "localTransactionManager")
public class PersistenceLocalAutoConfiguration {
	@Bean
    @ConfigurationProperties(prefix="spring.datasource.local")
    public DataSource localDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    // productEntityManager bean 
 
    // productTransactionManager bean
}
