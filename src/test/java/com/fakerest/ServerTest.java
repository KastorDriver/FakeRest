package com.fakerest;

import com.fakerest.bean.Response;
import com.fakerest.bean.Route;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class ServerTest {

    private static final String GET_PATH = "/getpath";
    private static final String GET = "get";
    private static final String RESPONSE_BODY = "response text";

    @Test
    public void testGetRequest() throws FileNotFoundException {
        Server.initRoute(prepareGetRoute());
        String response = new RestTemplate().getForObject("http://localhost:4567" + GET_PATH, String.class);
        assertEquals(RESPONSE_BODY, response);
    }

    private Route prepareGetRoute() {
        Route route = new Route();
        route.setPath(GET_PATH);
        route.setType(GET);

        Response response = new Response();
        response.setBody(RESPONSE_BODY);
        route.setResponse(response);

        return route;
    }
}
