package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Answer;
import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.bean.Cookie;
import one.kastordriver.fakerest.bean.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RouteProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(RouteProcessor.class);

    @Autowired
    private ConditionProcessor conditionProcessor;

    public Object process(Route route, Request request, Response response) throws Exception {
        try {
            Optional<Condition> suitableCondition = findFirstAppropriateCondition(route, request);
            return processAnswer(fetchAnswer(route, suitableCondition), response);
        } catch (Exception ex) {
            LOGGER.error("route process error", ex);
            throw ex;
        }
    }

    private Optional<Condition> findFirstAppropriateCondition(Route route, Request request) {
        return route.getConditions().stream()
                .filter(condition -> conditionProcessor.isConditionSuitForRequest(condition.getCondition(), request))
                .findFirst();
    }

    private Answer fetchAnswer(Route route, Optional<Condition> suitableCondition) {
        return suitableCondition.map(Condition::getAnswer).orElse(route.getAnswer());
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
