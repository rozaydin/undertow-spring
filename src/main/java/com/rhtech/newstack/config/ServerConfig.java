package com.rhtech.newstack.config;

import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ServerConfig {

    @Bean(name = "undertow", destroyMethod = "stop")
    public Undertow createServer(RoutingHandler handler) {

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost").setHandler(handler)
                .build();

        return server;
    }


    @Bean(name = "routingHandler")
    public RoutingHandler createRoutingHandler() {
        RoutingHandler routingHandler = new RoutingHandler();
        return routingHandler;
    }

}
