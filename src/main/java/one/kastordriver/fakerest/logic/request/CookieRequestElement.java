package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.springframework.stereotype.Component;
import spark.Request;

@Component
public class CookieRequestElement extends RequestElement {

    private static final String ELEMENT_NAME = "@cookie";
    private static final String REPLACED_ELEMENT_NAME = "_cookie";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String processCondition(String condition, Request request, Binding binding) {
        final String firstElementPart = ELEMENT_NAME + "(";
        final String lastElementPart = ")";
        int startIndex = condition.indexOf(firstElementPart);
        int endIndex = condition.indexOf(lastElementPart, startIndex);
        final String cookieName = condition.substring(startIndex + firstElementPart.length(), endIndex);
        final String fullElementName = firstElementPart.replace("(", "\\(") + cookieName + lastElementPart.replace(")", "\\)");

        final String replacedElementName = REPLACED_ELEMENT_NAME + cookieName;
        final String elementValue = request.cookie(cookieName);
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(fullElementName, replacedElementName);
    }
}
