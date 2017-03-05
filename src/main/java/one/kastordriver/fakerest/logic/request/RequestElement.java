package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import spark.Request;

public abstract class RequestElement {

    public abstract String processCondition(String condition, Request request, Binding binding);
    public abstract String getElementName();

    public boolean containedInCondition(String condition) {
        return condition.indexOf(getElementName()) != -1;
    }

}
