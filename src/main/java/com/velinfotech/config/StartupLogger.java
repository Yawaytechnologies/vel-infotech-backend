package com.velinfotech.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class StartupLogger implements CommandLineRunner {

    private final DataSource dataSource;
    private final Environment env;

    public StartupLogger(DataSource dataSource, Environment env) {
        this.dataSource = dataSource;
        this.env = env;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n======== SYSTEM STATUS =========");

        try (Connection conn = dataSource.getConnection()) {
            String dbUrl = conn.getMetaData().getURL();
            String dbName = conn.getCatalog(); // may be null for some DBs until you specify schema

            System.out.println("✅ DATABASE CONNECTED SUCCESSFULLY");
            System.out.println("🔗 JDBC URL: " + dbUrl);
            System.out.println("📂 Database Name: " + (dbName != null ? dbName : "(unknown)"));
            System.out.println("👤 DB User: " + conn.getMetaData().getUserName());
            System.out.println("🧩 Driver: " + conn.getMetaData().getDriverName() + " " + conn.getMetaData().getDriverVersion());
        } catch (Exception e) {
            System.out.println("❌ DATABASE CONNECTION FAILED: " + e.getMessage());
        }

        // read actual server.port (defaults to 8080 if not set)
        String port = env.getProperty("server.port", "8080");
        System.out.println("🌐 SERVER RUNNING AT: http://localhost:" + port + "/");
        System.out.println("=================================\n");
    }
}
