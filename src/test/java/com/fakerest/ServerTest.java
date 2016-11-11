package com.fakerest;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class ServerTest {

    private static final String GET_PATH = "/path1";
    private static final String GET = "get";
    private static final String RESPONSE_BODY = "response text";

    @Test
    public void testGetRequest() throws FileNotFoundException {
        Server.main(null);
        ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:4567" + GET_PATH, String.class);

        assertEquals(RESPONSE_BODY, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("text/xml", response.getHeaders().getFirst("Content-Type"));
    }


}
