package one.kastordriver.fakerest.entity;

import lombok.Getter;
import lombok.Setter;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class Route implements spark.Route {

    private String method;
    private String url;
    private Answer defaultAnswer = new Answer();
    private List<Condition> conditions = new ArrayList<>();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            Optional<Condition> suitableCondition = findFirstAppropriateCondition(request);
            return processAnswer(fetchAnswer(suitableCondition), response);
        } catch (Exception ex) {
            System.out.println(ex);
            throw ex;
        }
    }

    private Optional<Condition> findFirstAppropriateCondition(Request request) {
        return this.getConditions().stream()
            .filter(condition -> condition.isSuitable(request))
            .findFirst();
    }

    private Answer fetchAnswer(Optional<Condition> suitableCondition) {
        return suitableCondition.isPresent() ? suitableCondition.get().getAnswer() : this.getDefaultAnswer();
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
