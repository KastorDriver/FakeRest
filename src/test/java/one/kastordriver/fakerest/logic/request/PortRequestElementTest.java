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
public class PortRequestElementTest {

    private static final int PORT = 5555;

    @Mock
    private Request request;

    private PortRequestElement portRequestElement = new PortRequestElement();

    @Test
    void shouldBeSuitableForConditionWithPortRequestElement() {
        assertThat(portRequestElement.isContainedInCondition("@port == 5555"), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutPortRequestElement() {
        assertThat(portRequestElement.isContainedInCondition("@ip == 192.168.0.1"), equalTo(false));
    }

    @Test
    void shouldReplaceIpRequestElementPrefixWithUnderscore() {
        when(request.port()).thenReturn(PORT);

        Binding binding = new Binding();

        String processedConditionExpression = portRequestElement.processCondition("@port == 5555", request, binding);

        assertThat(processedConditionExpression, equalTo("_port == 5555"));
    }

    @Test
    void shouldBindActualPortToVariable() {
        when(request.port()).thenReturn(PORT);

        Binding binding = new Binding();

        portRequestElement.processCondition("@port == 5555", request, binding);

        assertThat(binding.getProperty("_port"), equalTo(PORT));
    }
}
