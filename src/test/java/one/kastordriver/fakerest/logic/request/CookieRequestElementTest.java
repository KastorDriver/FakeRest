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
public class CookieRequestElementTest {

    private static final String COOKIE_NAME = "nickname";
    private static final String COOKIE_VALUE = "Martin";

    @Mock
    private Request request;

    private CookieRequestElement cookieRequestElement = new CookieRequestElement();

    @Test
    void shouldBeSuitableForConditionWithCookieRequestElement() {
        assertThat(cookieRequestElement.isContainedInCondition(
                String.format("@cookie(%s) == \"%s\"", COOKIE_NAME, COOKIE_VALUE)), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutCookieRequestElement() {
        assertThat(cookieRequestElement.isContainedInCondition("@ip == \"127.0.0.1\""), equalTo(false));
    }

    @Test
    void shouldReplaceCookieRequestElementPrefixWithUnderscore() {
        when(request.cookie(COOKIE_NAME)).thenReturn(COOKIE_VALUE);

        Binding binding = new Binding();

        String processedConditionExpression = cookieRequestElement.processCondition(
                String.format("@cookie(%s) == \"%s\"", COOKIE_NAME, COOKIE_VALUE), request, binding);

        assertThat(processedConditionExpression, equalTo(String.format("$cookie%s == \"%s\"", COOKIE_NAME, COOKIE_VALUE)));
    }

    @Test
    void shouldBindActualCookieValueToVariable() {
        when(request.cookie(COOKIE_NAME)).thenReturn(COOKIE_VALUE);

        Binding binding = new Binding();

        cookieRequestElement.processCondition(String.format("@cookie(%s) == \"%s\"", COOKIE_NAME, COOKIE_VALUE), request, binding);

        assertThat(binding.getProperty(String.format("$cookie%s", COOKIE_NAME)), equalTo(COOKIE_VALUE));
    }
}
