package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeaderRequestElementTest {

    private static final String HEADER_NAME = "nickname";
    private static final String HEADER_VALUE = "Martin";

    @Mock
    private Request request;

    private HeaderRequestElement headerRequestElement = new HeaderRequestElement();

    @Test
    void shouldBeSuitableForConditionWithHeaderRequestElement() {
        assertThat(headerRequestElement.isContainedInCondition(
                String.format("@header(%s) == \"%s\"n", HEADER_NAME, HEADER_VALUE)), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutHeaderRequestElement() {
        assertThat(headerRequestElement.isContainedInCondition("@ip == \"127.0.0.1\""), equalTo(false));
    }

    @Test
    void shouldReplaceCookieRequestElementPrefixWithUnderscore() {
        when(request.headers(HEADER_NAME)).thenReturn(HEADER_VALUE);

        Binding binding = new Binding();

        String processedConditionExpression = headerRequestElement.processCondition(
                String.format("@header(%s) == \"%s\"", HEADER_NAME, HEADER_VALUE), request, binding);

        assertThat(processedConditionExpression, equalTo(String.format("$header%s == \"%s\"", HEADER_NAME, HEADER_VALUE)));
    }

    @Test
    void shouldBindActualHeaderValueToVariable() {
        when(request.headers(HEADER_NAME)).thenReturn(HEADER_VALUE);

        Binding binding = new Binding();

        headerRequestElement.processCondition(String.format("@header(%s) == \"%s\"", HEADER_NAME, HEADER_VALUE), request, binding);

        assertThat(binding.getProperty(String.format("$header%s", HEADER_NAME)), equalTo(HEADER_VALUE));
    }
}
