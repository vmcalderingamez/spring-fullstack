package com.amigoscode;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestcontainers {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLConteiner.getJdbcUrl(),
                postgreSQLConteiner.getUsername(),
                postgreSQLConteiner.getPassword()
        ).load();
        flyway.migrate();
        System.out.println();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLConteiner =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("amigoscode-dao-unit-test")
                    .withUsername("amigoscode")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgreSQLConteiner::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLConteiner::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLConteiner::getPassword
        );

    }
    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLConteiner.getDriverClassName())
                .url(postgreSQLConteiner.getJdbcUrl())
                .username(postgreSQLConteiner.getUsername())
                .password(postgreSQLConteiner.getPassword())
                .build();
    }
    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker faker = new Faker();
}
