package one.kastordriver.fakerest;

import one.kastordriver.fakerest.bean.Condition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Request;

public class ConditionTest {

    private Request requestMock;

    @Before
    public void before() {
        requestMock = Mockito.mock(Request.class);
    }

    @Test
    public void manyRequestsForTheSameConditionMustWorkFine() throws Exception {
        final String ip = "127.0.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);

        Condition condition = Condition.builder().condition(String.format("@ip == \"%s\"", ip)).build();
        Assert.assertTrue(condition.isSuitable(requestMock));
        Assert.assertTrue(condition.isSuitable(requestMock));
    }

    @Test
    public void ipConditionIsSuitable() {
        final String ip = "127.0.0.1";
        final String wrongIp = "192.168.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);
        Assert.assertTrue(Condition.builder().condition(String.format("@ip == \"%s\"", ip)).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format("@ip== \"%s\"", ip)).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format(" @ip == \"%s\"", ip)).build().isSuitable(requestMock));
        Assert.assertFalse(Condition.builder().condition(String.format(" @ip == \"%s\"", wrongIp)).build().isSuitable(requestMock));
    }

    @Test
    public void portConditionIsSuitable() {
        final int port = 8080;
        final int wrongPort = 9090;

        Mockito.when(requestMock.port()).thenReturn(port);
        Assert.assertTrue(Condition.builder().condition(String.format("@port == %s", port)).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format("@port== %s", port)).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format(" @port == %s", port)).build().isSuitable(requestMock));
        Assert.assertFalse(Condition.builder().condition(String.format(" @port == %s", wrongPort)).build().isSuitable(requestMock));
    }

    @Test
    public void contentLengthConditionIsSuitable() {
        final int contentLength = 1024;
        final int wrongContentLength = 2048;

        Mockito.when(requestMock.contentLength()).thenReturn(contentLength);
        Assert.assertTrue(Condition.builder().condition(String.format("@contentLength == %s", contentLength)).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format("@contentLength== %s", contentLength)).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format(" @contentLength == %s", contentLength)).build().isSuitable(requestMock));
        Assert.assertFalse(Condition.builder().condition(String.format(" @contentLength == %s", wrongContentLength)).build().isSuitable(requestMock));
    }

    @Test
    public void cookieConditionIsSuitable() {
        Mockito.when(requestMock.cookie("newCookie")).thenReturn("cookieValue");
        Assert.assertTrue(Condition.builder().condition(String.format("@cookie(newCookie) == \"%s\"", "cookieValue")).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format("@cookie(newCookie)== \"%s\"", "cookieValue")).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format(" @cookie(newCookie) == \"%s\"", "cookieValue")).build().isSuitable(requestMock));
        Assert.assertFalse(Condition.builder().condition(String.format("@cookie(newCookie) == \"%s\"", "wrongValue")).build().isSuitable(requestMock));
    }

    @Test
    public void cookieConditionIsSuitableForManyCookies() {
        Mockito.when(requestMock.cookie("newCookie")).thenReturn("cookieValue");
        Mockito.when(requestMock.cookie("anotherCookie")).thenReturn("anotherValue");

        Assert.assertTrue(Condition.builder().condition(
                String.format("@cookie(newCookie) == \"%s\" && @cookie(anotherCookie) == \"%s\"", "cookieValue", "anotherValue")
            ).build().isSuitable(requestMock));
    }

    @Test
    public void headerConditionIsSuitable() {
        Mockito.when(requestMock.headers("Accept")).thenReturn("application/json");
        Mockito.when(requestMock.headers("AnotherHeader")).thenReturn("anotherValue");

        Assert.assertTrue(Condition.builder().condition(String.format("@header(Accept) == \"%s\"", "application/json")).build().isSuitable(requestMock));
        Assert.assertTrue(Condition.builder().condition(String.format("@header(AnotherHeader) == \"%s\"", "anotherValue")).build().isSuitable(requestMock));
    }
}
