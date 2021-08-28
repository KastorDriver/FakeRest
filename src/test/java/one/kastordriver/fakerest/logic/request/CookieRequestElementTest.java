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

    private CookieRequestElement cookieRequestElement;

    @BeforeEach
    void setUp() {
        cookieRequestElement = new CookieRequestElement();

        when(request.cookie(COOKIE_NAME)).thenReturn(COOKIE_VALUE);
    }

    @Test
    void shouldReplaceCookieRequestElementPrefixWithUnderscore() {
        Binding binding = new Binding();

        String processedConditionExpression = cookieRequestElement.processCondition("@cookie(nickname) == Martin", request, binding);

        assertThat(processedConditionExpression, equalTo("_cookie(nickname) == Martin"));
    }

    @Test
    void shouldBindActualCookieValueToVariable() {
        Binding binding = new Binding();

        cookieRequestElement.processCondition("@cookie(nickname) == Martin", request, binding);

        assertThat(binding.getProperty("_cookie(nickname)"), equalTo(COOKIE_VALUE));
    }
}
