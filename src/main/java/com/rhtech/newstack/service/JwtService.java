package com.rhtech.newstack.service;

import com.rhtech.newstack.model.User;
import io.undertow.security.api.SecurityContext;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
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

    public String generateJwt(SecurityContext securityContext) throws JoseException {
        // get user data from security context
        User user = (User) securityContext.getAuthenticatedAccount();

        // create jwt claims
        JwtClaims claims = new JwtClaims();
        claims.setGeneratedJwtId(); // set jwt id
        claims.setIssuedAtToNow();  // iat
        claims.setSubject(user.getPrincipal().getName()); // sub
        claims.setExpirationTimeMinutesInTheFuture(60f);  // exp
        claims.setClaim("scopes", user.getRoles()); // scopes

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        // header
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        jws.setKeyIdHeaderValue("kid-1");
        // signature
        jws.setKey(keyPair.getPrivate());
        // create jws and return
        return jws.getCompactSerialization();
    }
}
