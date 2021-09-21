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
public class QueryParamRequestElementTest {

    private static final String QUERY_PARAM_NAME = "nickname";
    private static final String QUERY_PARAM_VALUE = "Martin";

    @Mock
    private Request request;

    private QueryParamRequestElement queryParamRequestElement = new QueryParamRequestElement();

    @Test
    void shouldBeSuitableForConditionWithPathParamRequestElement() {
        assertThat(queryParamRequestElement.isContainedInCondition(
                String.format("@queryParam(%s) == \"%s\"", QUERY_PARAM_NAME, QUERY_PARAM_VALUE)), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutPathParamRequestElement() {
        assertThat(queryParamRequestElement.isContainedInCondition("@ip == 192.168.0.1"), equalTo(false));
    }

    @Test
    void shouldReplaceQueryParamRequestElementPrefixWithUnderscore() {
        when(request.queryParams(QUERY_PARAM_NAME)).thenReturn(QUERY_PARAM_VALUE);

        Binding binding = new Binding();

        String processedConditionExpression = queryParamRequestElement.processCondition(
                String.format("@queryParam(%s) == \"%s\"", QUERY_PARAM_NAME, QUERY_PARAM_VALUE), request, binding);

        assertThat(processedConditionExpression, equalTo(String.format("$queryParam%s == \"%s\"", QUERY_PARAM_NAME, QUERY_PARAM_VALUE)));
    }

    @Test
    void shouldBindActualQueryParamValueToVariable() {
        when(request.queryParams(QUERY_PARAM_NAME)).thenReturn(QUERY_PARAM_VALUE);

        Binding binding = new Binding();

        queryParamRequestElement.processCondition(
                String.format("@queryParam(%s) == \"%s\"", QUERY_PARAM_NAME, QUERY_PARAM_VALUE), request, binding);

        assertThat(binding.getProperty(String.format("$queryParam%s", QUERY_PARAM_NAME)), equalTo(QUERY_PARAM_VALUE));
    }
}
