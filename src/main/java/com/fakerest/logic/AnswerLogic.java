package com.fakerest.logic;

import com.fakerest.bean.Answer;
import com.fakerest.bean.Cookie;
import com.fakerest.bean.Route;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

public class AnswerLogic {

    public static Object handle(Route route, Request request, Response response) throws Exception {
        Answer answer = route.getAnswer();

        processStatus(response, answer.getStatus());
        processHeaders(response, answer.getHeaders());
        processCookies(response, answer.getCookies());
        processRemoveCookies(response, answer.getRemoveCookies());

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

    private static void processCookies(Response response, List<Cookie> cookies) {
        cookies.forEach(cookie -> {
            response.cookie(cookie.getPath(), cookie.getName(), cookie.getValue(),
                    cookie.getMaxAge(), cookie.isSecure());
        });
    }

    private static void processRemoveCookies(Response response, List<String> cookiesForRemove) {
        cookiesForRemove.forEach(cookieForRemove -> response.removeCookie(cookieForRemove));
    }
}
