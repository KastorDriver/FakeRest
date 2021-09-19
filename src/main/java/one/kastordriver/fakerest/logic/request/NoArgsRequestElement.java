package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;

public abstract class NoArgsRequestElement extends RequestElement {

    protected String processNoArgsRequestCondition(String condition, Binding binding, Object elementValue) {
        final String originElementName = getOriginElementName();
        final String underscoredElementName = getUnderscoredElementName();

        binding.setVariable(underscoredElementName, elementValue);

        return condition.replaceAll(originElementName, underscoredElementName);
    }
}
