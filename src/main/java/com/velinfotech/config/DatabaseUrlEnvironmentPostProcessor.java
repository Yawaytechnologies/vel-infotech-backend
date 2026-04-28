package com.velinfotech.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String PROPERTY_SOURCE_NAME = "databaseUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String springDatasourceUrl = environment.getProperty("SPRING_DATASOURCE_URL");
        String databaseUrl = environment.getProperty("DATABASE_URL");

        logEnvironmentStatus(environment, springDatasourceUrl, databaseUrl);

        if (StringUtils.hasText(springDatasourceUrl)) {
            System.out.println("[database-config] Using SPRING_DATASOURCE_URL: " + maskUrl(springDatasourceUrl));
            return;
        }

        if (!StringUtils.hasText(databaseUrl)) {
            System.out.println("[database-config] DATABASE_URL not provided. Falling back to application.properties datasource URL.");
            return;
        }

        Map<String, Object> properties = new LinkedHashMap<>();
        if (databaseUrl.startsWith("jdbc:")) {
            properties.put("spring.datasource.url", databaseUrl);
        } else if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            addPostgresProperties(databaseUrl, environment, properties);
        }

        if (!properties.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
            System.out.println("[database-config] Using normalized DATABASE_URL: "
                    + maskUrl(String.valueOf(properties.get("spring.datasource.url"))));
        } else {
            System.out.println("[database-config] DATABASE_URL was provided but was not a supported PostgreSQL URL.");
        }
    }

    private void addPostgresProperties(
            String databaseUrl,
            ConfigurableEnvironment environment,
            Map<String, Object> properties
    ) {
        URI uri = URI.create(databaseUrl);
        if (!StringUtils.hasText(uri.getHost())) {
            return;
        }

        StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://")
                .append(uri.getHost());

        if (uri.getPort() != -1) {
            jdbcUrl.append(':').append(uri.getPort());
        }

        jdbcUrl.append(StringUtils.hasText(uri.getPath()) ? uri.getPath() : "/postgres");

        if (StringUtils.hasText(uri.getQuery())) {
            jdbcUrl.append('?').append(uri.getQuery());
        }

        properties.put("spring.datasource.url", jdbcUrl.toString());

        String userInfo = uri.getRawUserInfo();
        if (StringUtils.hasText(userInfo)) {
            String[] credentials = userInfo.split(":", 2);
            if (!StringUtils.hasText(environment.getProperty("DB_USERNAME"))) {
                properties.put("spring.datasource.username", decode(credentials[0]));
            }
            if (credentials.length > 1 && !StringUtils.hasText(environment.getProperty("DB_PASSWORD"))) {
                properties.put("spring.datasource.password", decode(credentials[1]));
            }
        }
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private void logEnvironmentStatus(
            ConfigurableEnvironment environment,
            String springDatasourceUrl,
            String databaseUrl
    ) {
        System.out.println("[database-config] SPRING_DATASOURCE_URL present: " + StringUtils.hasText(springDatasourceUrl));
        System.out.println("[database-config] DATABASE_URL present: " + StringUtils.hasText(databaseUrl));
        System.out.println("[database-config] DB_USERNAME present: " + StringUtils.hasText(environment.getProperty("DB_USERNAME")));
        System.out.println("[database-config] DB_PASSWORD present: " + StringUtils.hasText(environment.getProperty("DB_PASSWORD")));
    }

    private String maskUrl(String value) {
        return value
                .replaceAll("(?i)(password=)[^&]+", "$1****")
                .replaceAll("(//[^:/@]+:)[^@]+(@)", "$1****$2");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
