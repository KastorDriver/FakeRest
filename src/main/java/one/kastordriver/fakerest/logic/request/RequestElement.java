package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import spark.Request;

public abstract class RequestElement {

    private static final Character ORIGIN_ELEMENT_NAME_PREFIX = '@';
    private static final Character DOLLAR_ELEMENT_NAME_PREFIX = '$';

    private static final String PARENTHESIS_REGEX = "[()]";
    private static final String EMPTY_STRING = "";

    public boolean isContainedInCondition(String condition) {
        //TODO NPE?
        return condition.indexOf(getElementName()) != -1;
    }

    public abstract String processCondition(String condition, Request request, Binding binding);

    protected abstract String getElementName();

    protected String transformRequestElementToGroovyFriendlyVariablev(String requestElement) {
        return requestElement.replace(ORIGIN_ELEMENT_NAME_PREFIX, DOLLAR_ELEMENT_NAME_PREFIX)
                .replaceAll(PARENTHESIS_REGEX, EMPTY_STRING);
    }
}
