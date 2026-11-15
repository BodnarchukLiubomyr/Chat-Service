package com.example.chat_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class MainDatabaseConfig {

    @Bean(name = "mainDataSource")
    public DataSource mainDataSource(
            @Value("${app.datasource.main.url}") String url,
            @Value("${app.datasource.main.username}") String username,
            @Value("${app.datasource.main.password}") String password) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
