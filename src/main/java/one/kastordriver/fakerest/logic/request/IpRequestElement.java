package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.springframework.stereotype.Component;
import spark.Request;

@Component
public class IpRequestElement extends RequestElement {

    private static final String ELEMENT_NAME = "@ip";
    private static final String REPLACED_ELEMENT_NAME = "_ip";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String processCondition(String condition, Request request, Binding binding) {
        final String elementValue = request.ip();
        binding.setVariable(REPLACED_ELEMENT_NAME, elementValue);
        return condition.replaceAll(ELEMENT_NAME, REPLACED_ELEMENT_NAME);
    }
}
