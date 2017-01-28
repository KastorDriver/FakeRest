package com.fakerest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Server.class)
public class ServerTest {

    private static final String URL = "http://localhost:4567";

    @Test
    public void simpleGetRequest() throws Exception {
        final String RESPONSE_TEXT = "response text";
        final int STATUS_CODE = 200;

        String route = "--- !com.fakerest.bean.Route\n" +
                "method: get\n" +
                "url: /path1\n" +
                "defaultAnswer: !com.fakerest.bean.Answer\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n";

        PowerMockito.stub(PowerMockito.method(Server.class, "loadRoutesFilesIntoString")).toReturn(route);
        Server.main(null);
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + "/path1", String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());
        assertEquals(RESPONSE_TEXT, response.getBody());
    }

//    @Test
//    public void testGetRequest() throws IOException {
//        Server.main(null);
//        ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:4567" + PATH, String.class);
//
//        assertEquals(RESPONSE_BODY, response.getBody());
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("text/html", response.getHeaders().getFirst("Content-Type"));
//        List<String> cookies = response.getHeaders().get("Set-Cookie");
//        assertEquals("cookie1=value1;Path=path1", cookies.get(0));
//        assertEquals("cookie2=value2;Secure", cookies.get(1));
//        assertTrue(cookies.get(2).contains("cookieForRemove1=\"\"")
//                && cookies.get(2).contains("Max-Age=0"));
//        assertTrue(cookies.get(3).contains("cookieForRemove2=\"\"")
//                && cookies.get(3).contains("Max-Age=0"));
//    }
}
