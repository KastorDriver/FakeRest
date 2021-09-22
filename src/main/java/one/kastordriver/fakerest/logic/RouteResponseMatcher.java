package one.kastordriver.fakerest.logic;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.kastordriver.fakerest.model.RouteResponse;
import one.kastordriver.fakerest.model.Condition;
import one.kastordriver.fakerest.model.Route;
import one.kastordriver.fakerest.logic.request.RequestElement;
import org.springframework.stereotype.Component;
import spark.Request;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class RouteResponseMatcher {

    private final List<RequestElement> requestElements;

    public RouteResponse findAppropriateRouteResponse(Route route, Request request) {
        return findAnswerForFirstMatchedCondition(route, request)
                .orElse(route.getResponse());
    }

    private Optional<RouteResponse> findAnswerForFirstMatchedCondition(Route route, Request request) {
        return route.getConditions().stream()
                .filter(condition -> isConditionSuitableForRequest(condition.getCondition(), request))
                .map(Condition::getResponse)
                .findFirst();
    }

    private boolean isConditionSuitableForRequest(String condition, Request request) {
        Binding binding = new Binding();
        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(substituteRequestElementsWithValues(condition, request, binding));

        if (result instanceof Boolean) {
            return (Boolean)result;
        } else {
            log.error("Condition \"{}\" doesn't return boolean type", condition);
            return false;
        }
    }

    private String substituteRequestElementsWithValues(String condition, Request request, Binding binding) {
        for (RequestElement requestElement : requestElements) {
            while (requestElement.isContainedInCondition(condition)) {
                condition = requestElement.processCondition(condition, request, binding);
            }
        }

        return condition;
    }
}
