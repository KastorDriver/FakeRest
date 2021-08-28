package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.springframework.stereotype.Component;
import spark.Request;

@Component
public class CookieRequestElement extends SingleArgRequestElement {

    private static final String ELEMENT_NAME = "cookie";

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String processCondition(String condition, Request request, Binding binding) {
        return processCondition(condition, binding, request::cookie);
    }
}
