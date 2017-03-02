package com.fakerest.bean;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import spark.Request;

@Builder
@Getter
@Setter
public class Condition {
    private String condition;
    private Answer answer;

    @Tolerate
    public Condition() {}

    public boolean isSuitable(Request request) {
        Binding binding = new Binding();

        for (RequestElement requestElement : RequestElement.values()) {
            while (requestElement.containedInCondition(condition)) {
                condition = requestElement.getProcessCondition().apply(condition, request, binding);
            }
        }

        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(condition);
        return result instanceof Boolean ? (Boolean)result : false;
    }
}
