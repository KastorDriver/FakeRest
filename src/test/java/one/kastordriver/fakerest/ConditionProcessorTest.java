package one.kastordriver.fakerest;

import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.logic.ConditionProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Request;

public class ConditionProcessorTest {

    private Request requestMock;
    private static ConditionProcessor conditionProcessor;

    @BeforeClass
    public static void beforeClass() {
        conditionProcessor = new ConditionProcessor();
    }

    @Before
    public void before() {
        requestMock = Mockito.mock(Request.class);
    }

    @Test
    public void manyRequestsForTheSameConditionMustWorkFine() throws Exception {
        final String ip = "127.0.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);

        Condition condition = Condition.builder().condition(String.format("@ip == \"%s\"", ip)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void ipConditionIsSuitable() {
        final String ip = "127.0.0.1";
        final String wrongIp = "192.168.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);

        Condition condition = Condition.builder().condition(String.format("@ip == \"%s\"", ip)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@ip== \"%s\"", ip)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @ip == \"%s\"", ip)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @ip == \"%s\"", wrongIp)).build();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void portConditionIsSuitable() {
        final int port = 8080;
        final int wrongPort = 9090;

        Mockito.when(requestMock.port()).thenReturn(port);

        Condition condition = Condition.builder().condition(String.format("@port == %s", port)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@port== %s", port)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @port == %s", port)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@port == %s", wrongPort)).build();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void contentLengthConditionIsSuitable() {
        final int contentLength = 1024;
        final int wrongContentLength = 2048;

        Mockito.when(requestMock.contentLength()).thenReturn(contentLength);

        Condition condition = Condition.builder().condition(String.format("@contentLength == %s", contentLength)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@contentLength== %s", contentLength)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @contentLength == %s", contentLength)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @contentLength == %s", wrongContentLength)).build();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void cookieConditionIsSuitable() {
        final String cookieValue = "cookieValue";
        final String wrongCookieValue = "wrongCookieValue";
        Mockito.when(requestMock.cookie("newCookie")).thenReturn(cookieValue);

        Condition condition = Condition.builder().condition(String.format("@cookie(newCookie) == \"%s\"", cookieValue)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@cookie(newCookie)== \"%s\"", cookieValue)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format(" @cookie(newCookie) == \"%s\"", cookieValue)).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@cookie(newCookie) == \"%s\"", wrongCookieValue)).build();
        Assert.assertFalse(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void cookieConditionIsSuitableForManyCookies() {
        Mockito.when(requestMock.cookie("newCookie")).thenReturn("cookieValue");
        Mockito.when(requestMock.cookie("anotherCookie")).thenReturn("anotherValue");


        Condition condition = Condition.builder().condition(
                String.format("@cookie(newCookie) == \"%s\" && @cookie(anotherCookie) == \"%s\"", "cookieValue", "anotherValue")
        ).build();

        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }

    @Test
    public void headerConditionIsSuitable() {
        Mockito.when(requestMock.headers("Accept")).thenReturn("application/json");
        Mockito.when(requestMock.headers("AnotherHeader")).thenReturn("anotherValue");

        Condition condition = Condition.builder().condition(String.format("@header(Accept) == \"%s\"", "application/json")).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));

        condition = Condition.builder().condition(String.format("@header(AnotherHeader) == \"%s\"", "anotherValue")).build();
        Assert.assertTrue(conditionProcessor.isConditionSuitForRequest(condition, requestMock));
    }
}
