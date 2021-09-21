package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;

public abstract class NoArgsRequestElement extends RequestElement {

    protected String processNoArgsRequestCondition(String condition, Binding binding, Object elementValue) {
        final String groovyFriendlyVariableName = transformRequestElementToGroovyFriendlyVariablev(getElementName());
        binding.setVariable(groovyFriendlyVariableName, elementValue);
        return condition.replace(getElementName(), groovyFriendlyVariableName);
    }
}
