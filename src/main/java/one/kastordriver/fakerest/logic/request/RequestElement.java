package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import spark.Request;

import java.util.function.Function;

public abstract class RequestElement {

    private static final Character ORIGIN_ELEMENT_NAME_PREFIX = '@';
    private static final Character REPLACED_ELEMENT_NAME_PREFIX = '_';

    public abstract String processCondition(String condition, Request request, Binding binding);
    public abstract String getElementName();

    private String getOriginElementName() {
        return ORIGIN_ELEMENT_NAME_PREFIX + getElementName();
    }

    private String getReplacedElementName() {
        return REPLACED_ELEMENT_NAME_PREFIX + getElementName();
    };

    public boolean containedInCondition(String condition) {
        return condition.indexOf(getOriginElementName()) != -1;
    }

    public String processSingleRequestElement(String condition, Binding binding, Object elementValue) {
        final String replacedElementName = getReplacedElementName();
        final String originElementName = getOriginElementName();
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(originElementName, replacedElementName);
    }

    public String processComplexRequestElement(String condition, Binding binding, Function<String, Object> extractRequestParamValue) {
        final String firstElementPart = getOriginElementName() + "(";
        final String lastElementPart = ")";
        int startIndex = condition.indexOf(firstElementPart);
        int endIndex = condition.indexOf(lastElementPart, startIndex);
        final String requestParamName = condition.substring(startIndex + firstElementPart.length(), endIndex);
        final String fullElementName = firstElementPart.replace("(", "\\(") + requestParamName + lastElementPart.replace(")", "\\)");
        final String replacedElementName = getReplacedElementName() + requestParamName;

        binding.setVariable(replacedElementName, extractRequestParamValue.apply(requestParamName));
        return condition.replaceAll(fullElementName, replacedElementName);
    }
}
