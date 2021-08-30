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
public class IpRequestElementTest {

    private static final String IP = "192.168.0.1";

    @Mock
    private Request request;

    private IpRequestElement ipRequestElement = new IpRequestElement();

    @Test
    void shouldBeSuitableForConditionWithIpRequestElement() {
        assertThat(ipRequestElement.isContainedInCondition("@ip == 192.168.0.1"), equalTo(true));
    }

    @Test
    void shouldNotBeSuitableForConditionWithoutIpRequestElement() {
        assertThat(ipRequestElement.isContainedInCondition("@header(nickname) == Martin"), equalTo(false));
    }

    @Test
    void shouldReplaceIpRequestElementPrefixWithUnderscore() {
        when(request.ip()).thenReturn(IP);

        Binding binding = new Binding();

        String processedConditionExpression = ipRequestElement.processCondition("@ip == 192.168.0.1", request, binding);

        assertThat(processedConditionExpression, equalTo("_ip == 192.168.0.1"));
    }

    @Test
    void shouldBindActualIpToVariable() {
        when(request.ip()).thenReturn(IP);

        Binding binding = new Binding();

        ipRequestElement.processCondition("@ip == 192.168.0.1", request, binding);

        assertThat(binding.getProperty("_ip"), equalTo(IP));
    }
}
