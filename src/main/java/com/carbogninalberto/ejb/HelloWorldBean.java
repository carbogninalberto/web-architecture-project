package com.carbogninalberto.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class HelloWorldBean {

    private int counter;

    public HelloWorldBean() {
        this.counter = 0;
    }

    public String sayHello(String name) {
        counter++;
        return "Hello World! You are " + name + " amd counter is " + counter;
    }
}
