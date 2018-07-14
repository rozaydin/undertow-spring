package com.rhtech.newstack.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalExceptionHandler implements HttpHandler {

    private final HttpHandler next;

    public GlobalExceptionHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        try {
            log.info("Request received!");
            next.handleRequest(httpServerExchange);
        } catch (Exception e) {
            log.error("Exception occured while processing the request", e);
            
            if (httpServerExchange.isResponseChannelAvailable()) {
                // handle error
            }
        }
    }
}
