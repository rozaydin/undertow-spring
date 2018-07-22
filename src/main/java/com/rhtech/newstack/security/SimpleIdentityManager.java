package com.rhtech.newstack.security;

import com.rhtech.newstack.model.User;
import com.sun.security.auth.UserPrincipal;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;

import java.util.HashSet;
import java.util.Set;

public class SimpleIdentityManager implements IdentityManager {

    @Override
    public Account verify(Account account) {
        return null;
    }

    @Override
    public Account verify(String id, Credential credential) {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        return new User(new UserPrincipal(id), roles);
    }

    @Override
    public Account verify(Credential credential) {
        return null;
    }
}
