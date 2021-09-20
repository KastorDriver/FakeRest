package one.kastordriver.fakerest.logic;

import lombok.extern.slf4j.Slf4j;
import one.kastordriver.fakerest.model.Answer;
import one.kastordriver.fakerest.model.Cookie;
import one.kastordriver.fakerest.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RouteProcessor {

    @Autowired
    private AnswerMatcher answerMatcher;

    public Object process(Route route, Request request, Response response) {
        try {
            return processAnswer(answerMatcher.findAppropriateAnswer(route, request), response);
        } catch (Exception ex) {
            log.error("route process error", ex);
            throw ex;
        }
    }

    private Object processAnswer(Answer answer, Response response) {
        processStatus(response, answer.getStatus());
        processHeaders(response, answer.getHeaders());
        processCookies(response, answer.getCookies());
        processRemoveCookies(response, answer.getRemoveCookies());

        return answer.getBody();
    }

    private void processStatus(Response response, Integer status) {
        response.status(status);
    }

    private void processHeaders(Response response, Map<String, String> headers) {
        headers.forEach((key, value) -> response.header(key, value));
    }

    private void processCookies(Response response, List<Cookie> cookies) {
        cookies.forEach(cookie -> {
            response.cookie(cookie.getPath(), cookie.getName(), cookie.getValue(),
                    cookie.getMaxAge(), cookie.isSecure());
        });
    }

    private void processRemoveCookies(Response response, List<String> cookiesForRemove) {
        cookiesForRemove.forEach(cookieForRemove -> response.removeCookie(cookieForRemove));
    }
}
