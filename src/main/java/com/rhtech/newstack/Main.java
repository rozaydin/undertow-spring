package com.rhtech.newstack;

import io.undertow.Undertow;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        // initialize container
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("com.rhtech.newstack");

        Undertow server = ((Undertow)applicationContext.getBean("undertow"));
        server.start();
    }

}
