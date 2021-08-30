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
        assertThat(headerRequestElement.isContainedInCondition("@header(nickname) == Martin"), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutHeaderRequestElement() {
        assertThat(headerRequestElement.isContainedInCondition("@ip == 192.168.0.1"), equalTo(false));
    }

    @Test
    void shouldReplaceCookieRequestElementPrefixWithUnderscore() {
        when(request.headers(HEADER_NAME)).thenReturn(HEADER_VALUE);

        Binding binding = new Binding();

        String processedConditionExpression = headerRequestElement.processCondition("@header(nickname) == Martin", request, binding);

        assertThat(processedConditionExpression, equalTo("_header(nickname) == Martin"));
    }

    @Test
    void shouldBindActualHeaderValueToVariable() {
        when(request.headers(HEADER_NAME)).thenReturn(HEADER_VALUE);

        Binding binding = new Binding();

        headerRequestElement.processCondition("@header(nickname) == Martin", request, binding);

        assertThat(binding.getProperty("_header(nickname)"), equalTo(HEADER_VALUE));
    }
}
