package com.rhtech.newstack.model;

import io.undertow.security.idm.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class User implements Account {

    private final Principal principal;
    private final Set<String> roles;

}
