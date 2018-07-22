package com.rhtech.newstack.config;

import com.google.gson.Gson;
import com.rhtech.newstack.handler.GlobalExceptionHandler;
import com.rhtech.newstack.security.AuthenticationRequiredDecider;
import com.rhtech.newstack.security.JWTAuthentication;
import com.rhtech.newstack.security.SimpleIdentityManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.undertow.Undertow;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;

@Configuration
@PropertySource("classpath:application.properties")
public class ServerConfig {

    @Bean(name = "undertow", destroyMethod = "stop")
    public Undertow createServer(
            @Qualifier("processingChain") HttpHandler handler,
            @Value("${server.hostname}") String hostname,
            @Value("${server.port}") int port) {
        return Undertow.builder()
                .setIoThreads(1)
                .setWorkerThreads(10)
                .addHttpListener(port, hostname)
                .setHandler(handler)
                .build();
    }

    /**
     * This method constructs the processing chain for the application
     * Each handler calls the other, ordering is done programmatically
     *
     * @param routingHandler
     * @return
     */
    @Bean(name = "processingChain")
    public HttpHandler createProcessingChain(RoutingHandler routingHandler) {
        // create processing chain for the application

        // authentication handlers
        HttpHandler handler = new AuthenticationCallHandler(routingHandler);
        // handler = new AuthenticationConstraintHandler(handler);
        handler = new AuthenticationRequiredDecider(handler);
        handler = new AuthenticationMechanismsHandler(handler, Collections.singletonList(new JWTAuthentication()));
        handler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, new SimpleIdentityManager(), handler);
        // Request Buffering
        // handler = new RequestBufferingHandler(handler, 100);

        // global exception handler
        return new GlobalExceptionHandler(handler);
    }

    @Bean(name = "routingHandler")
    public RoutingHandler createRoutingHandler() {
        RoutingHandler routingHandler = new RoutingHandler();
        return routingHandler;
    }

    @Bean
    public HikariConfig createHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/world_x");
        config.setUsername("root");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }

    @Bean
    public DataSource createDataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public KeyPair createKeyPair(@Value("${jks.alias}") String jksAlias, @Value("${jks.password}") String jksPassword) throws Exception {

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("undertowApp.jks");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(is, jksPassword.toCharArray());

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(jksAlias, jksPassword.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(jksAlias).getPublicKey();
        return new KeyPair(publicKey, privateKey);
    }

    @Bean
    public Gson createGson() {
        return new Gson();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer(
            Environment env) {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
                new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setEnvironment(env);
        return propertySourcesPlaceholderConfigurer;
    }
}
