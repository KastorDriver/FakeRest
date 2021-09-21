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

    private PathParamRequestElement pathParamRequestElement = new PathParamRequestElement();

    @Test
    void shouldBeSuitableForConditionWithPathParamRequestElement() {
        assertThat(pathParamRequestElement.isContainedInCondition(
                String.format("@pathParam(%s) == \"%s\"", PATH_PARAM_NAME, PATH_PARAM_VALUE)), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutPathParamRequestElement() {
        assertThat(pathParamRequestElement.isContainedInCondition("@ip == 192.168.0.1"), equalTo(false));
    }

    @Test
    void shouldReplacePathParamRequestElementPrefixWithUnderscore() {
        when(request.params(PATH_PARAM_NAME)).thenReturn(PATH_PARAM_VALUE);

        Binding binding = new Binding();

        String processedConditionExpression = pathParamRequestElement.processCondition(
                String.format("@pathParam(%s) == \"%s\"", PATH_PARAM_NAME, PATH_PARAM_VALUE), request, binding);

        assertThat(processedConditionExpression, equalTo(String.format("$pathParam%s == \"%s\"", PATH_PARAM_NAME, PATH_PARAM_VALUE)));
    }

    @Test
    void shouldBindActualPathParamValueToVariable() {
        when(request.params(PATH_PARAM_NAME)).thenReturn(PATH_PARAM_VALUE);

        Binding binding = new Binding();

        pathParamRequestElement.processCondition(
                String.format("@pathParam(%s) == \"%s\"", PATH_PARAM_NAME, PATH_PARAM_VALUE), request, binding);

        assertThat(binding.getProperty(String.format("$pathParam%s", PATH_PARAM_NAME)), equalTo(PATH_PARAM_VALUE));
    }
}
