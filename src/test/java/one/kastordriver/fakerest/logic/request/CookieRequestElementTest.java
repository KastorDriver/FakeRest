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
        assertThat(cookieRequestElement.isContainedInCondition("@cookie(nickname) == Martin"), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutCookieRequestElement() {
        assertThat(cookieRequestElement.isContainedInCondition("@ip == 192.168.0.1"), equalTo(false));
    }

    @Test
    void shouldReplaceCookieRequestElementPrefixWithUnderscore() {
        when(request.cookie(COOKIE_NAME)).thenReturn(COOKIE_VALUE);

        Binding binding = new Binding();

        String processedConditionExpression = cookieRequestElement.processCondition("@cookie(nickname) == Martin", request, binding);

        assertThat(processedConditionExpression, equalTo("_cookienickname == Martin"));
    }

    @Test
    void shouldBindActualCookieValueToVariable() {
        when(request.cookie(COOKIE_NAME)).thenReturn(COOKIE_VALUE);

        Binding binding = new Binding();

        cookieRequestElement.processCondition("@cookie(nickname) == Martin", request, binding);

        assertThat(binding.getProperty("_cookienickname"), equalTo(COOKIE_VALUE));
    }
}
