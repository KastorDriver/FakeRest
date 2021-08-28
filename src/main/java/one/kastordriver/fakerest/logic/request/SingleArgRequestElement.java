package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;

import java.util.function.Function;

public abstract class SingleArgRequestElement extends RequestElement {

    public String processCondition(String condition, Binding binding, Function<String, Object> extractRequestParamValue) {
        final String firstElementPart = getOriginElementName() + "(";
        final String lastElementPart = ")";
        int startIndex = condition.indexOf(firstElementPart);
        int endIndex = condition.indexOf(lastElementPart, startIndex);
        final String requestParamName = condition.substring(startIndex + firstElementPart.length(), endIndex);
        final String fullElementName = firstElementPart.replace("(", "\\(") + requestParamName + lastElementPart.replace(")", "\\)");
        final String replacedElementName = getReplacedElementName() + "(" + requestParamName + ")";

        binding.setVariable(replacedElementName, extractRequestParamValue.apply(requestParamName));
        return condition.replaceAll(fullElementName, replacedElementName);
    }
}
