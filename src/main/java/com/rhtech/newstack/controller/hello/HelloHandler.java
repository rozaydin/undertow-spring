package com.rhtech.newstack.controller.hello;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloHandler implements HttpHandler {

    @Value("${name}")
    private String test;

    @Autowired
    public HelloHandler(RoutingHandler routingHandler) {
        // Register handlers
        routingHandler.get("/hello", this::handleRequest);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseSender().send(test);
    }
}
