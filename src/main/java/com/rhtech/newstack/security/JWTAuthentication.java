package com.rhtech.newstack.security;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthentication implements AuthenticationMechanism {

    @Override
    public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange, SecurityContext securityContext) {
        String headerValue = exchange.getRequestHeaders().get("Authorization").getFirst();
        //
        log.info("header value is: {}", headerValue);
        return AuthenticationMechanismOutcome.AUTHENTICATED;
    }

    @Override
    public ChallengeResult sendChallenge(HttpServerExchange exchange, SecurityContext securityContext) {
        log.info("send challange called!");
        return null;
    }
}
