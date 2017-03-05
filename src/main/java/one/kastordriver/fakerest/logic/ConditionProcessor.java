package one.kastordriver.fakerest.logic;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.bean.RequestElement;
import org.springframework.stereotype.Component;
import spark.Request;

@Component
public class ConditionProcessor {

    public boolean isConditionSuitForRequest(Condition condition, Request request) {
        Binding binding = new Binding();
        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(prepareConditionToEvaluate(condition.getCondition(), binding, request));
        return result instanceof Boolean ? (Boolean)result : false;
    }

    private String prepareConditionToEvaluate(String condition, Binding binding, Request request) {
        for (RequestElement requestElement : RequestElement.values()) {
            while (requestElement.containedInCondition(condition)) {
                condition = requestElement.getProcessCondition().apply(condition, request, binding);
            }
        }

        return condition;
    }
}
