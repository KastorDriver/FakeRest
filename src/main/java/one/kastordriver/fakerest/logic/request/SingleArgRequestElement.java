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
        final String fullElementName = firstElementPart.replace("(", "\\(") + requestParamName + CLOSING_PARENTHESIS.replace(")", "\\)");
        final String replacedElementName = getUnderscoredElementName() + "(" + requestParamName + ")";

        binding.setVariable(replacedElementName, extractRequestParamValue.apply(requestParamName));
        return condition.replaceAll(fullElementName, replacedElementName);
    }
}
