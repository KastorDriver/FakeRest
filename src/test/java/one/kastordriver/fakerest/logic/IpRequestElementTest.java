package one.kastordriver.fakerest.logic;

import groovy.lang.Binding;
import one.kastordriver.fakerest.logic.request.IpRequestElement;
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
public class IpRequestElementTest {

    @Mock
    private Request request;

    private IpRequestElement ipRequestElement;

    @BeforeEach
    void setUp() {
        ipRequestElement = new IpRequestElement();

        when(request.ip()).thenReturn("192.168.0.1");
    }

    @Test
    void shouldReplaceIpRequestElementPrefixToUnderscore() {
        Binding binding = new Binding();

        String processedConditionExpression = ipRequestElement.processCondition("@ip == 192.168.0.1", request, binding);

        assertThat(processedConditionExpression, equalTo("_ip == 192.168.0.1"));
    }

    @Test
    void shouldBindActualIpToVariable() {
        Binding binding = new Binding();

        ipRequestElement.processCondition("@ip == 192.168.0.1", request, binding);

        assertThat(binding.getProperty("_ip"), equalTo("192.168.0.1"));
    }
}
