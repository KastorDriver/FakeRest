package one.kastordriver.fakerest.bean;

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
            Optional<Condition> suitableCondition = this.getConditions().stream()
                    .filter(condition -> condition.isSuitable(request))
                    .findFirst();

            return processAnswer(suitableCondition.isPresent() ? suitableCondition.get().getAnswer()
                    : this.getDefaultAnswer(), response);
        } catch (Exception ex) {
            System.out.println(ex);
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
        if (status != null) {
            response.status(status);
        }
    }

    private void processHeaders(Response response, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach((key, value) -> response.header(key, value));
        }
    }

    private void processCookies(Response response, List<Cookie> cookies) {
        if (cookies != null) {
            cookies.forEach(cookie -> {
                response.cookie(cookie.getPath(), cookie.getName(), cookie.getValue(),
                        cookie.getMaxAge(), cookie.isSecure());
            });
        }
    }

    private void processRemoveCookies(Response response, List<String> cookiesForRemove) {
        if (cookiesForRemove != null) {
            cookiesForRemove.forEach(cookieForRemove -> response.removeCookie(cookieForRemove));
        }
    }
}
