package com.rhtech.newstack.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Country {

    private final String Code;
    private final String Name;
    private final String Continent;

}
