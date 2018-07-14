package com.rhtech.newstack.config;

import com.rhtech.newstack.handler.GlobalExceptionHandler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.undertow.Undertow;
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
   * @param routingHandler
   * @return
   */
  @Bean(name = "processingChain")
  public HttpHandler createProcessingChain(RoutingHandler routingHandler) {
    // create processing chain for the application
    GlobalExceptionHandler handler = new GlobalExceptionHandler(routingHandler);
    return handler;
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
  public static PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer(
      Environment env) {
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
        new PropertySourcesPlaceholderConfigurer();
    propertySourcesPlaceholderConfigurer.setEnvironment(env);
    return propertySourcesPlaceholderConfigurer;
  }
}
