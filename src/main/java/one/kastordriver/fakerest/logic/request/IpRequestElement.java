package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.springframework.stereotype.Component;
import spark.Request;

@Component
public class IpRequestElement extends NoArgsRequestElement {

    private static final String ELEMENT_NAME = "@ip";

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String processCondition(String condition, Request request, Binding binding) {
        return processNoArgsRequestCondition(condition, binding, request.ip());
    }
}
