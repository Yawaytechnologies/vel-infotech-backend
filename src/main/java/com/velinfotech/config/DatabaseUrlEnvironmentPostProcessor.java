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

        if (StringUtils.hasText(springDatasourceUrl)) {
            return;
        }

        if (!StringUtils.hasText(databaseUrl)) {
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

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
