package com.fakerest.logic;

import com.fakerest.bean.Answer;
import com.fakerest.bean.Condition;
import com.fakerest.bean.Cookie;
import com.fakerest.bean.Route;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnswerLogic {

    public static Object handle(Route route, Request request, Response response) throws Exception {
        try {
            Optional<Condition> condition = route.getConditions().stream()
                    .filter(condt -> ConditionLogic.isSuitable(condt.getCondition(), request))
                    .findFirst();

            return processAnswer(condition.isPresent() ? condition.get().getAnswer() : route.getDefaultAnswer(), response);
        } catch (Exception ex) {
            System.out.println(ex);
            throw ex;
        }
    }

    public static void main(String[] args) {
        Binding binding = new Binding();
        binding.setVariable("ip", "127.0.0.1");
        GroovyShell groovyShell = new GroovyShell(binding);
        System.out.println(groovyShell.evaluate("ip == '127.0.0.1'"));
    }

    private static Object processAnswer(Answer answer, Response response) {
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
        if (headers != null) {
            headers.forEach((key, value) -> response.header(key, value));
        }
    }

    private static void processCookies(Response response, List<Cookie> cookies) {
        if (cookies != null) {
            cookies.forEach(cookie -> {
                response.cookie(cookie.getPath(), cookie.getName(), cookie.getValue(),
                        cookie.getMaxAge(), cookie.isSecure());
            });
        }
    }

    private static void processRemoveCookies(Response response, List<String> cookiesForRemove) {
        if (cookiesForRemove != null) {
            cookiesForRemove.forEach(cookieForRemove -> response.removeCookie(cookieForRemove));
        }
    }
}
