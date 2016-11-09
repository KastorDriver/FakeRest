package com.fakerest.logic;

import com.fakerest.bean.Answer;
import com.fakerest.bean.Route;
import spark.Request;
import spark.Response;

import java.util.Map;

public class AnswerLogic {

    public static Object handle(Route route, Request request, Response response) throws Exception {
        Answer answer = route.getAnswer();

        processStatus(response, answer.getStatus());
        processHeaders(response, answer.getHeaders());

        return answer.getBody();
    }

    private static void processStatus(Response response, Integer status) {
        if (status != null) {
            response.status(status);
        }
    }

    private static void processHeaders(Response response, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, value) -> response.header(key, value));
        }
    }
}
