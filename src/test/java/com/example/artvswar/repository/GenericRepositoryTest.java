package com.example.artvswar.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
abstract class GenericRepositoryTest {

    private static final String DB = "db";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
    private static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    private static final String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName(DB)
            .withUsername(USER)
            .withPassword(PASSWORD);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add(SPRING_DATASOURCE_URL, mySQLContainer::getJdbcUrl);
        registry.add(SPRING_DATASOURCE_USERNAME, mySQLContainer::getUsername);
        registry.add(SPRING_DATASOURCE_PASSWORD, mySQLContainer::getPassword);
    }
}
