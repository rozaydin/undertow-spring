package com.rhtech.newstack.security;

import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationRequiredDecider extends AuthenticationConstraintHandler {

    public AuthenticationRequiredDecider(HttpHandler next) {
        super(next);
    }

    @Override
    protected boolean isAuthenticationRequired(HttpServerExchange exchange) {

        String requestPath = exchange.getRequestPath();
        log.info("Request Path: {}, checking if authentication is required", requestPath);

        // decide if auth required
        switch (requestPath) {
            case "/login":
                return false;
            default:
                return true;
        }
    }
}
