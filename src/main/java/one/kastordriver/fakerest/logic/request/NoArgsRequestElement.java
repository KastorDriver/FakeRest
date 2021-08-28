package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;

public abstract class NoArgsRequestElement extends RequestElement {

    protected String processNoArgsRequestCondition(String condition, Binding binding, Object elementValue) {
        final String replacedElementName = getReplacedElementName();
        final String originElementName = getOriginElementName();
        binding.setVariable(replacedElementName, elementValue);
        return condition.replaceAll(originElementName, replacedElementName);
    }
}
