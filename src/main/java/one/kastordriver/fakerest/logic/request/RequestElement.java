package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import spark.Request;

import java.util.function.Function;

public abstract class RequestElement {

    private static final Character ORIGIN_ELEMENT_NAME_PREFIX = '@';
    private static final Character REPLACED_ELEMENT_NAME_PREFIX = '_';

    public abstract String processCondition(String condition, Request request, Binding binding);

    protected abstract String getElementName();

    protected String getOriginElementName() {
        return ORIGIN_ELEMENT_NAME_PREFIX + getElementName();
    }

    protected String getReplacedElementName() {
        return REPLACED_ELEMENT_NAME_PREFIX + getElementName();
    };

    public boolean containedInCondition(String condition) {
        return condition.indexOf(getOriginElementName()) != -1;
    }

}
