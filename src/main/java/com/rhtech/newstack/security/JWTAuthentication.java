package com.rhtech.newstack.security;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthentication implements AuthenticationMechanism {

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
                    // validate token
                    return AuthenticationMechanismOutcome.AUTHENTICATED;
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
