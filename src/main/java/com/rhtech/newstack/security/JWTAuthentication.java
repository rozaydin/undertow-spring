package com.rhtech.newstack.security;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;

@Slf4j
public class JWTAuthentication implements AuthenticationMechanism {

    private final JwtConsumer jwtConsumer;

    public JWTAuthentication(JwtConsumer jwtConsumer) {
        this.jwtConsumer = jwtConsumer;
    }

    @Override
    public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange, SecurityContext securityContext) {

        // if already authenticated
        if (!securityContext.isAuthenticationRequired() || securityContext.isAuthenticated()) {
            return AuthenticationMechanismOutcome.AUTHENTICATED;
        } else {// perform authentication
            HeaderMap requestHeaders = exchange.getRequestHeaders();
            HeaderValues authorizationHeaderValue = requestHeaders.get("Authorization");

            if (authorizationHeaderValue != null) {
                String jwtToken = authorizationHeaderValue.getFirst();
                if (jwtToken != null) {
                    try {
                        // validate token
                        jwtConsumer.process(jwtToken);
                        return AuthenticationMechanismOutcome.AUTHENTICATED;
                    } catch (InvalidJwtException ijwtExc) {
                        log.warn("jwt validation failed", ijwtExc);
                    }
                }
            }
            // auth failed - return auth required
            return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
        }
    }

    @Override
    public ChallengeResult sendChallenge(HttpServerExchange exchange, SecurityContext securityContext) {
        return new ChallengeResult(true, 401);
    }
}
