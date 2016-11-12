package com.fakerest;

import com.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals("text/html", response.getHeaders().getFirst("Content-Type"));
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        assertEquals("cookie1=value1;Path=path1", cookies.get(0));
        assertEquals("cookie2=value2;Secure", cookies.get(1));
        assertTrue(cookies.get(2).contains("cookieForRemove1=\"\"")
                && cookies.get(2).contains("Max-Age=0"));
        assertTrue(cookies.get(3).contains("cookieForRemove2=\"\"")
                && cookies.get(3).contains("Max-Age=0"));
    }

    @Test
    public void marazm() throws FileNotFoundException {
        Yaml.loadStreamOfType(new File("routes.yaml"), Route.class).forEach(route -> {
            System.out.println(route.getAnswer().getCookies().get(0));
        });
    }


}
