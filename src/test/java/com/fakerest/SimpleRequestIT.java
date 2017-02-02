package com.fakerest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Server.class)
public class SimpleRequestIT {

    private static final String LOAD_ROUTES_FUNC_NAME = "loadRoutesFilesIntoString";
    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";
    private static final String RESPONSE_TEXT = "response text";

    @Before
    public void before() {
        PATH = PATH + System.currentTimeMillis();
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleGetRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "--- !com.fakerest.bean.Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !com.fakerest.bean.Answer\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n";

        PowerMockito.stub(PowerMockito.method(Server.class, LOAD_ROUTES_FUNC_NAME)).toReturn(route);
        Server.main(null);
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());
        assertEquals(RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctHeadersForSimpleGetRequest() throws Exception {
        final String ACCEPT = "Accept";
        final String CONTENT_TYPE = "Content-Type";

        String route = "--- !com.fakerest.bean.Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !com.fakerest.bean.Answer\n" +
                "  headers:\n" +
                "    " + ACCEPT + ": " + MediaType.TEXT_PLAIN_VALUE + "\n" +
                "    " + CONTENT_TYPE + ": " + MediaType.APPLICATION_JSON_VALUE + "\n";

        PowerMockito.stub(PowerMockito.method(Server.class, LOAD_ROUTES_FUNC_NAME)).toReturn(route);
        Server.main(null);

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        assertEquals(MediaType.TEXT_PLAIN_VALUE, headers.getFirst(ACCEPT));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
    }

    @Test
    public void correctRemoveCookieForSimpleGetRequest() throws Exception {
        final String COOKIE = "cookieForRemove";

        String route = "--- !com.fakerest.bean.Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !com.fakerest.bean.Answer\n" +
                "  removeCookies: [" + COOKIE + "]\n";

        PowerMockito.stub(PowerMockito.method(Server.class, LOAD_ROUTES_FUNC_NAME)).toReturn(route);
        Server.main(null);

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.getFirst("Set-Cookie");
        assertTrue(setCookie.contains(COOKIE+"=\"\""));
        assertTrue(setCookie.contains("Max-Age=0"));
    }

    @Test
    public void correctSetCookieForSimpleGetRequest() throws Exception {
        final String COOKIE = "cookieForAdd";
        final String COOKIE_VALUE = "someValue";
        final String COOKIE_PATH = "somePath";

        String route = "--- !com.fakerest.bean.Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !com.fakerest.bean.Answer\n" +
                "  cookies:\n" +
                        "    - !com.fakerest.bean.Cookie\n" +
                        "      path: " + COOKIE_PATH + "\n" +
                        "      name: " + COOKIE + "\n" +
                        "      value: " + COOKIE_VALUE + "\n" +
                        "      maxAge: 3600\n" +
                        "      secure: true\n";

        PowerMockito.stub(PowerMockito.method(Server.class, LOAD_ROUTES_FUNC_NAME)).toReturn(route);
        Server.main(null);

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.get("Set-Cookie").toString();
        assertTrue(setCookie.contains(String.format("%s=%s;Path=%s", COOKIE, COOKIE_VALUE, COOKIE_PATH)));
        assertTrue(setCookie.contains("Expire"));
        assertTrue(setCookie.contains("Secure"));
    }
}
