package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;

import java.util.function.Function;

public abstract class SingleArgRequestElement extends RequestElement {

    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSING_PARENTHESIS = ")";

    protected String processSingleArgRequestCondition(String condition, Binding binding, Function<String, Object> extractRequestParamValue) {
        final String firstElementPart = getOriginElementName() + OPEN_PARENTHESIS;

        int startIndex = condition.indexOf(firstElementPart);
        int endIndex = condition.indexOf(CLOSING_PARENTHESIS, startIndex);

        final String requestParamName = condition.substring(startIndex + firstElementPart.length(), endIndex);
        final String fullElementName = firstElementPart + requestParamName + CLOSING_PARENTHESIS;
        final String underscoredElementName = getUnderscoredElementName() + OPEN_PARENTHESIS + requestParamName + CLOSING_PARENTHESIS;

        binding.setVariable(underscoredElementName, extractRequestParamValue.apply(requestParamName));
        return condition.replace(fullElementName, underscoredElementName);
    }
}
