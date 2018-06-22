package com.rhtech.newstack.config;

import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class ServerConfig {

  @Autowired Environment env;

  @Bean(name = "undertow", destroyMethod = "stop")
  public Undertow createServer(RoutingHandler handler) {
    return Undertow.builder().addHttpListener(8080, "localhost").setHandler(handler).build();
  }

  @Bean(name = "routingHandler")
  public RoutingHandler createRoutingHandler() {
    RoutingHandler routingHandler = new RoutingHandler();
    return routingHandler;
  }
}
