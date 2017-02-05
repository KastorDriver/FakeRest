package com.fakerest.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import spark.Request;

public class ConditionLogicTest {

    private Request requestMock;

    @Before
    public void before() {
        requestMock = Mockito.mock(Request.class);
    }

    @Test
    public void ipConditionIsSuitable() {
        final String ip = "127.0.0.1";
        final String wrongIp = "192.168.0.1";

        Mockito.when(requestMock.ip()).thenReturn(ip);
        Assert.assertTrue(ConditionLogic.isSuitable(String.format("@ip == \"%s\"", ip), requestMock));
        Assert.assertTrue(ConditionLogic.isSuitable(String.format("@ip== \"%s\"", ip), requestMock));
        Assert.assertTrue(ConditionLogic.isSuitable(String.format(" @ip == \"%s\"", ip), requestMock));
        Assert.assertFalse(ConditionLogic.isSuitable(String.format(" @ip == \"%s\"", wrongIp), requestMock));
    }

    @Test
    public void portConditionIsSuitable() {
        final int port = 8080;
        final int wrongPort = 9090;

        Mockito.when(requestMock.port()).thenReturn(port);
        Assert.assertTrue(ConditionLogic.isSuitable(String.format("@port == %s", port), requestMock));
        Assert.assertTrue(ConditionLogic.isSuitable(String.format("@port== %s", port), requestMock));
        Assert.assertTrue(ConditionLogic.isSuitable(String.format(" @port == %s", port), requestMock));
        Assert.assertFalse(ConditionLogic.isSuitable(String.format(" @port == %s", wrongPort), requestMock));
    }

    @Test
    public void contentLengthConditionIsSuitable() {
        final int contentLength = 1024;
        final int wrongContentLength = 2048;

        Mockito.when(requestMock.contentLength()).thenReturn(contentLength);
        Assert.assertTrue(ConditionLogic.isSuitable(String.format("@contentLength == %s", contentLength), requestMock));
        Assert.assertTrue(ConditionLogic.isSuitable(String.format("@contentLength== %s", contentLength), requestMock));
        Assert.assertTrue(ConditionLogic.isSuitable(String.format(" @contentLength == %s", contentLength), requestMock));
        Assert.assertFalse(ConditionLogic.isSuitable(String.format(" @contentLength == %s", wrongContentLength), requestMock));
    }
}
