package com.rhtech.newstack.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class ServerConfig {

  @Bean(name = "undertow", destroyMethod = "stop")
  public Undertow createServer(
      RoutingHandler handler,
      @Value("${server.hostname}") String hostname,
      @Value("${server.port}") int port) {
    return Undertow.builder().addHttpListener(port, hostname).setHandler(handler).build();
  }

  @Bean(name = "routingHandler")
  public RoutingHandler createRoutingHandler() {
    RoutingHandler routingHandler = new RoutingHandler();
    return routingHandler;
  }

  @Bean
  public HikariConfig createHikariConfig() {
    HikariConfig config = new HikariConfig();
    //@todo do set properties here
    return config;
  }

  @Bean
  public DataSource createDataSource(HikariConfig hikariConfig) {
    return new HikariDataSource(hikariConfig);
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
