package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.model.RouteResponse;
import one.kastordriver.fakerest.model.Condition;
import one.kastordriver.fakerest.model.Route;
import one.kastordriver.fakerest.logic.request.HeaderRequestElement;
import one.kastordriver.fakerest.logic.request.IpRequestElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteResponseMatcherTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final String JOHN_WICK = "John Wick";

    private RouteResponseMatcher routeResponseMatcher;

    @Mock
    private Request request;

    @Test
    void ifRouteDoesNotHaveConditionsThenReturnDefaultRouteResponse() {
        routeResponseMatcher = new RouteResponseMatcher(asList());

        RouteResponse defaultRouteResponse = RouteResponse.builder()
                .body("Default routeResponse")
                .build();

        Route route = Route.builder()
                .response(defaultRouteResponse)
                .conditions(emptyList())
                .build();

        assertThat(routeResponseMatcher.findAppropriateRouteResponse(route, request), equalTo(defaultRouteResponse));
    }

    @Test
    void shouldReturnRouteResponseForMatchedCondition() {
        routeResponseMatcher = new RouteResponseMatcher(asList(new IpRequestElement()));

        when(request.ip()).thenReturn(LOCALHOST);

        RouteResponse defaultRouteResponse = RouteResponse.builder()
                .body("Default routeResponse")
                .build();

        RouteResponse conditionRouteResponse = RouteResponse.builder()
                .body("Condition routeResponse")
                .build();

        Route route = Route.builder()
                .response(defaultRouteResponse)
                .conditions(asList(new Condition(String.format("@ip == \"%s\"", LOCALHOST), conditionRouteResponse)))
                .build();

        assertThat(routeResponseMatcher.findAppropriateRouteResponse(route, request), equalTo(conditionRouteResponse));
    }

    @Test
    void ifMoreThanOneConditionsAreMatchedThenReturnRouteResponseForTheFirstMatchedCondition() {
        routeResponseMatcher = new RouteResponseMatcher(asList(new IpRequestElement(), new HeaderRequestElement()));

        when(request.headers("name")).thenReturn(JOHN_WICK);

        RouteResponse defaultRouteResponse = RouteResponse.builder()
                .body("Default routeResponse")
                .build();

        RouteResponse headerConditionRouteResponse = RouteResponse.builder()
                .body("header condition routeResponse")
                .build();

        RouteResponse ipConditionRouteResponse = RouteResponse.builder()
                .body("ip condition routeResponse")
                .build();

        Route route = Route.builder()
                .response(defaultRouteResponse)
                .conditions(asList(
                        new Condition(String.format("@header(name) == \"%s\"", JOHN_WICK), headerConditionRouteResponse),
                        new Condition(String.format("@ip == \"%s\"", LOCALHOST), ipConditionRouteResponse)))
                .build();

        assertThat(routeResponseMatcher.findAppropriateRouteResponse(route, request), equalTo(headerConditionRouteResponse));
    }

    @Test
    void ifNoneOfConditionsAreMatchedThenReturnDefaultRouteResponse() {
        routeResponseMatcher = new RouteResponseMatcher(asList(new IpRequestElement(), new HeaderRequestElement()));

        when(request.headers("name")).thenReturn("unexpected value");
        when(request.ip()).thenReturn("unexpected value");

        RouteResponse defaultRouteResponse = RouteResponse.builder()
                .body("Default routeResponse")
                .build();

        RouteResponse headerConditionRouteResponse = RouteResponse.builder()
                .body("header condition routeResponse")
                .build();

        RouteResponse ipConditionRouteResponse = RouteResponse.builder()
                .body("ip condition routeResponse")
                .build();

        Route route = Route.builder()
                .response(defaultRouteResponse)
                .conditions(asList(
                        new Condition(String.format("@header(name) == \"%s\"", JOHN_WICK), headerConditionRouteResponse),
                        new Condition(String.format("@ip == \"%s\"", LOCALHOST), ipConditionRouteResponse)))
                .build();

        assertThat(routeResponseMatcher.findAppropriateRouteResponse(route, request), equalTo(defaultRouteResponse));
    }
}
