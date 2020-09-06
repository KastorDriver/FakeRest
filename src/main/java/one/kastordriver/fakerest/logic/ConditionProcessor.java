package one.kastordriver.fakerest.logic;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import one.kastordriver.fakerest.logic.request.RequestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;

import java.util.List;

@Slf4j
@Component
public class ConditionProcessor {

    @Autowired
    private List<RequestElement> requestElements;

    public boolean isConditionSuitForRequest(String condition, Request request) {
        Binding binding = new Binding();
        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(prepareConditionToEvaluate(condition, binding, request));

        if (result instanceof Boolean) {
            return (Boolean)result;
        } else {
            log.error("Condition \"{}\" doesn't return boolean type", condition);
            return false;
        }
    }

    private String prepareConditionToEvaluate(String condition, Binding binding, Request request) {
        for (RequestElement requestElement : requestElements) {
            while (requestElement.containedInCondition(condition)) {
                condition = requestElement.processCondition(condition, request, binding);
            }
        }

        return condition;
    }
}
