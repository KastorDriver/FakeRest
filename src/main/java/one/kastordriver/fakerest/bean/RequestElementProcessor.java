package one.kastordriver.fakerest.bean;

import groovy.lang.Binding;
import spark.Request;

@FunctionalInterface
public interface RequestElementProcessor {
    String apply(String condition, Request request, Binding binding);
}
