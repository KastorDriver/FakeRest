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
public class PathParamRequestElementTest {

    private static final String PATH_PARAM_NAME = "nickname";
    private static final String PATH_PARAM_VALUE = "Martin";

    @Mock
    private Request request;

    private PathParamRequestElement pathParamRequestElement;

    @BeforeEach
    void setUp() {
        pathParamRequestElement = new PathParamRequestElement();

        when(request.params(PATH_PARAM_NAME)).thenReturn(PATH_PARAM_VALUE);
    }

    @Test
    void shouldReplacePathParamRequestElementPrefixWithUnderscore() {
        Binding binding = new Binding();

        String processedConditionExpression = pathParamRequestElement.processCondition("@pathParam(nickname) == Martin", request, binding);

        assertThat(processedConditionExpression, equalTo("_pathParam(nickname) == Martin"));
    }

    @Test
    void shouldBindActualPathParamValueToVariable() {
        Binding binding = new Binding();

        pathParamRequestElement.processCondition("@pathParam(nickname) == Martin", request, binding);

        assertThat(binding.getProperty("_pathParam(nickname)"), equalTo(PATH_PARAM_VALUE));
    }
}
