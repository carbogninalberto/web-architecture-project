package com.carbogninalberto.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class HelloWorldBean {

    public HelloWorldBean() {
    }

    public String sayHello(String name) {
        return "Hello World! You are " + name;
    }
}
