package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.config.AppConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spark.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ConditionProcessorTest {

    private Request requestMock;

    @Autowired
    private ConditionProcessor conditionProcessor;

    @Before
    public void before() {
        requestMock = Mockito.mock(Request.class);
    }

    @Test
    public void manyRequestsForTheSameConditionMustWorkFine() throws Exception {
        final String ip = "127.0.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);

        String condition = Condition.builder().condition(String.format("@ip == \"%s\"", ip)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void ipConditionIsSuitable() {
        final String ip = "127.0.0.1";
        final String wrongIp = "192.168.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);

        String condition = Condition.builder().condition(String.format("@ip == \"%s\"", ip)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@ip== \"%s\"", ip)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @ip == \"%s\"", ip)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @ip == \"%s\"", wrongIp)).build().getCondition();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void portConditionIsSuitable() {
        final int port = 8080;
        final int wrongPort = 9090;

        Mockito.when(requestMock.port()).thenReturn(port);

        String condition = Condition.builder().condition(String.format("@port == %s", port)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@port== %s", port)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @port == %s", port)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@port == %s", wrongPort)).build().getCondition();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void contentLengthConditionIsSuitable() {
        final int contentLength = 1024;
        final int wrongContentLength = 2048;

        Mockito.when(requestMock.contentLength()).thenReturn(contentLength);

        String condition = Condition.builder().condition(String.format("@contentLength == %s", contentLength)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@contentLength== %s", contentLength)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @contentLength == %s", contentLength)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @contentLength == %s", wrongContentLength)).build().getCondition();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void cookieConditionIsSuitable() {
        final String cookieValue = "cookieValue";
        final String wrongCookieValue = "wrongCookieValue";
        Mockito.when(requestMock.cookie("newCookie")).thenReturn(cookieValue);

        String condition = Condition.builder().condition(String.format("@cookie(newCookie) == \"%s\"", cookieValue)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@cookie(newCookie)== \"%s\"", cookieValue)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @cookie(newCookie) == \"%s\"", cookieValue)).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@cookie(newCookie) == \"%s\"", wrongCookieValue)).build().getCondition();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void cookieConditionIsSuitableForManyCookies() {
        Mockito.when(requestMock.cookie("newCookie")).thenReturn("cookieValue");
        Mockito.when(requestMock.cookie("anotherCookie")).thenReturn("anotherValue");


        String condition = Condition.builder().condition(
                String.format("@cookie(newCookie) == \"%s\" && @cookie(anotherCookie) == \"%s\"", "cookieValue", "anotherValue")
        ).build().getCondition();

        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void headerConditionIsSuitable() {
        Mockito.when(requestMock.headers("Accept")).thenReturn("application/json");
        Mockito.when(requestMock.headers("AnotherHeader")).thenReturn("anotherValue");

        String condition = Condition.builder().condition(String.format("@header(Accept) == \"%s\"", "application/json")).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@header(AnotherHeader) == \"%s\"", "anotherValue")).build().getCondition();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }
}
