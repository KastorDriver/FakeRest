package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.exception.RouteProcessingException;
import one.kastordriver.fakerest.model.Cookie;
import one.kastordriver.fakerest.model.Route;
import one.kastordriver.fakerest.model.RouteResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteProcessorTest {

    private static final int STATUS_CODE = 200;

    private static final String NICKNAME = "nickname";
    private static final String JOHN_WICK = "John Wick";
    private static final String EMAIL = "email";
    private static final String JOHN_WICK_EMAIL = "johnwick@gmail.com";
    public static final String COOKIE_PATH = "/somePath";
    public static final int COOKIE_MAX_AGE = 1024;
    public static final boolean SECURE_COOKIE = true;

    @Mock
    private RouteResponseMatcher routeResponseMatcher;

    @Mock
    private Route route;

    @Mock
    private Request request;

    @Mock
    private Response response;

    private RouteProcessor routeProcessor;

    @BeforeEach
    void setUp() {
        routeProcessor = new RouteProcessor(routeResponseMatcher);
        reset(routeResponseMatcher, route, request, response);
    }

    @Test
    void whenGetErrorByProcessingRouteThenThrowRouteProcessingException() {
        final RuntimeException unexpectedException = new RuntimeException("Unexpected exception");
        when(routeResponseMatcher.findAppropriateRouteResponse(any(), any())).thenThrow(unexpectedException);

        RouteProcessingException ex = Assertions.assertThrows(RouteProcessingException.class,
                () -> routeProcessor.process(route, request, response));

        assertThat(ex.getMessage(), equalTo("route processing error"));
        assertThat(ex.getCause(), equalTo(unexpectedException));
    }

    @Test
    void shouldSetResponseStatusCode() {
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setStatus(STATUS_CODE);
        when(routeResponseMatcher.findAppropriateRouteResponse(route, request)).thenReturn(routeResponse);

        routeProcessor.process(route, request, response);
        verify(response, times(1)).status(STATUS_CODE);
    }

    @Test
    void shouldSetResponseBody() {
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setBody(JOHN_WICK);
        when(routeResponseMatcher.findAppropriateRouteResponse(route, request)).thenReturn(routeResponse);

        String responseBody = routeProcessor.process(route, request, response);
        assertThat(responseBody, equalTo(JOHN_WICK));
    }

    @Test
    void whenResponseStatusCodeIsNotSpecifiedThenSetOkStatusCodeByDefault() {
        RouteResponse routeResponse = new RouteResponse();
        when(routeResponseMatcher.findAppropriateRouteResponse(route, request)).thenReturn(routeResponse);

        routeProcessor.process(route, request, response);
        verify(response, times(1)).status(STATUS_CODE);
    }

    @Test
    void shouldSetHeadersInResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put(NICKNAME, JOHN_WICK);
        headers.put(EMAIL, JOHN_WICK_EMAIL);

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setHeaders(headers);
        when(routeResponseMatcher.findAppropriateRouteResponse(route, request)).thenReturn(routeResponse);

        routeProcessor.process(route, request, response);
        verify(response, times(1)).header(NICKNAME, JOHN_WICK);
        verify(response, times(1)).header(EMAIL, JOHN_WICK_EMAIL);
    }

    @Test
    void shouldSetCookiesInResponse() {
        Cookie nicknameCookie= new Cookie(COOKIE_PATH, NICKNAME, JOHN_WICK, COOKIE_MAX_AGE, SECURE_COOKIE);

        Cookie emailCookie= new Cookie();
        emailCookie.setName(EMAIL);
        emailCookie.setValue(JOHN_WICK_EMAIL);

        List<Cookie> cookies = asList(nicknameCookie, emailCookie);

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setCookies(cookies);
        when(routeResponseMatcher.findAppropriateRouteResponse(route, request)).thenReturn(routeResponse);

        routeProcessor.process(route, request, response);
        verify(response, times(1)).cookie(null, EMAIL, JOHN_WICK_EMAIL, -1, false);
        verify(response, times(1)).cookie(COOKIE_PATH, NICKNAME, JOHN_WICK, COOKIE_MAX_AGE, SECURE_COOKIE);
    }

    @Test
    void shouldRemoveCookiesInResponse() {
        List<String> cookiesForRemoval = asList(NICKNAME, EMAIL);

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setRemoveCookies(cookiesForRemoval);
        when(routeResponseMatcher.findAppropriateRouteResponse(route, request)).thenReturn(routeResponse);

        routeProcessor.process(route, request, response);
        verify(response, times(1)).removeCookie(NICKNAME);
        verify(response, times(1)).removeCookie(EMAIL);
    }
}
