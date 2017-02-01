package com.fakerest.logic;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import spark.Request;

import java.util.function.BiFunction;

public class ConditionLogic {

    public static boolean isSuitable(String condition, Request request) {
        Binding binding = new Binding();

        for (ConditionElement conditionElement : ConditionElement.values()) {
            if (conditionElement.containedInCondition(condition)) {
                condition = conditionElement.getProcess().apply(condition, request);
            }
        }

        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(condition);
        return result instanceof Boolean ? (Boolean)result : false;
    }

    private enum ConditionElement {
        ip((condition, request) -> {
            return condition.replaceAll("ip", String.format("\"%s\"", request.ip()));
        });

        private BiFunction<String, Request, String> process;

        ConditionElement(BiFunction<String, Request, String> process) {
            this.process = process;
        }

        //TODO should be smarter (consider ConditionElement in the beginning of the sentence)
        //also need to symbols around ConditionElement (e.g. "ip==" or "func(ip)")
        public boolean containedInCondition(String condition) {
            return condition.indexOf(this.name()) != -1;
        }

        public BiFunction<String, Request,String> getProcess() {
            return process;
        }
    }
}
