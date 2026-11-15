package com.example.chat_service.config;

import com.example.chat_service.service.impl.V2__migrate_existing_data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class FlywayConfig {

    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer(
            @Qualifier("mainDataSource") DataSource mainDataSource) {

        JdbcTemplate mainJdbcTemplate = new JdbcTemplate(mainDataSource);

        return configuration -> configuration.javaMigrations(
                new V2__migrate_existing_data(mainJdbcTemplate)
        );
    }
}
