package one.kastordriver.fakerest.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.kastordriver.fakerest.exception.RouteProcessingException;
import one.kastordriver.fakerest.model.RouteResponse;
import one.kastordriver.fakerest.model.Cookie;
import one.kastordriver.fakerest.model.Route;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class RouteProcessor {

    private final RouteResponseMatcher routeResponseMatcher;

    public String process(Route route, Request request, Response response) {
        try {
            return processRouteResponse(response, routeResponseMatcher.findAppropriateRouteResponse(route, request));
        } catch (Exception ex) {
            log.error("route processing error", ex);
            throw new RouteProcessingException("route processing error", ex);
        }
    }

    private String processRouteResponse(Response response, RouteResponse routeResponse) {
        processStatus(response, routeResponse.getStatus());
        processHeaders(response, routeResponse.getHeaders());
        processCookies(response, routeResponse.getCookies());
        processRemoveCookies(response, routeResponse.getRemoveCookies());

        return routeResponse.getBody();
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
