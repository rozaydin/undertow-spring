package com.rhtech.newstack.service;

import io.undertow.security.api.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

@Service
public class JwtService {

    private final KeyPair keyPair;

    @Autowired
    public JwtService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String generateJwt(SecurityContext securityContext) {
        // get user data from security context
        securityContext.getAuthenticatedAccount();
        return "sample valid jwt!";
    }
}
