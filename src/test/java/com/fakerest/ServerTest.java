package com.fakerest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTest {

    private static final String PATH = "/path1";
    private static final String GET = "get";
    private static final String RESPONSE_BODY = "response text";

    @Test
    @DisplayName("dasdsad")
    void testGetRequest() throws IOException {
        Server.main(null);
        ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:4567" + PATH, String.class);

        assertEquals(RESPONSE_BODY, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("text/html", response.getHeaders().getFirst("Content-Type"));
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        assertEquals("cookie1=value1;Path=path1", cookies.get(0));
        assertEquals("cookie2=value2;Secure", cookies.get(1));
        assertTrue(cookies.get(2).contains("cookieForRemove1=\"\"")
                && cookies.get(2).contains("Max-Age=0"));
        assertTrue(cookies.get(3).contains("cookieForRemove2=\"\"")
                && cookies.get(3).contains("Max-Age=0"));
    }
}
