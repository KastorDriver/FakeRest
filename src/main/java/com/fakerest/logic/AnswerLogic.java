package com.fakerest.logic;

import com.fakerest.bean.Answer;
import com.fakerest.bean.Route;
import spark.Request;
import spark.Response;

public class AnswerLogic {

    public static Object handle(Route route, Request request, Response response) throws Exception {
        Answer answer = route.getAnswer();

        Integer status = answer.getStatus();
        if (status != null) {
            response.status(status);
        }

        return answer.getBody();
    }
}
