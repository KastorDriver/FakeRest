package com.fakerest;

import com.fakerest.bean.Answer;
import com.fakerest.bean.Route;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        route.setMethod(GET);

        Answer answer = new Answer();
        answer.setBody(RESPONSE_BODY);
        route.setAnswer(answer);

        return route;
    }
}
