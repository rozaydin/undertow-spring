package com.rhtech.newstack.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class LoginRequestData {

    private final String username;
    private final String password;

}
