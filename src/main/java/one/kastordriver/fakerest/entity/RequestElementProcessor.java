package one.kastordriver.fakerest.entity;

import groovy.lang.Binding;
import spark.Request;

/**
 * Created by Kastor on 02.03.2017.
 */
@FunctionalInterface
public interface RequestElementProcessor {
    String apply(String condition, Request request, Binding binding);
}
