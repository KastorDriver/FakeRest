package com.fakerest.logic;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import spark.Request;

public class ConditionLogic {

    public static boolean isConditioned(String condition, Request request) {
        Binding binding = new Binding();

        if (condition.contains("ip")) {
            binding.setVariable("ip", request.ip());
        }

        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(condition);
        return result instanceof Boolean ? (Boolean)result : false;
    }
}
