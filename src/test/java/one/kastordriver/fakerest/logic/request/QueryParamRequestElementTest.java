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

    private QueryParamRequestElement queryParamRequestElement;

    @BeforeEach
    void setUp() {
        queryParamRequestElement = new QueryParamRequestElement();

        when(request.queryParams(QUERY_PARAM_NAME)).thenReturn(QUERY_PARAM_VALUE);
    }

    @Test
    void shouldReplaceQueryParamRequestElementPrefixWithUnderscore() {
        Binding binding = new Binding();

        String processedConditionExpression = queryParamRequestElement.processCondition("@queryParam(nickname) == Martin", request, binding);

        assertThat(processedConditionExpression, equalTo("_queryParam(nickname) == Martin"));
    }

    @Test
    void shouldBindActualQueryParamValueToVariable() {
        Binding binding = new Binding();

        queryParamRequestElement.processCondition("@queryParam(nickname) == Martin", request, binding);

        assertThat(binding.getProperty("_queryParam(nickname)"), equalTo(QUERY_PARAM_VALUE));
    }
}
