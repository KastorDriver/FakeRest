package one.kastordriver.fakerest.logic;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FakeRestAnswerTest {

    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";
    private static final String RESPONSE_TEXT = "response text";
    private FakeRest fakeRest;

    @Before
    public void before() {
        PATH = PATH + System.currentTimeMillis();
        fakeRest = spy(FakeRest.class);
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleGetRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "defaultAnswer:\n" +
                       "  status: " + STATUS_CODE + "\n" +
                       "  body: " + RESPONSE_TEXT + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());
        assertEquals(RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctHeadersForSimpleGetRequest() throws Exception {
        final String ACCEPT = "Accept";
        final String CONTENT_TYPE = "Content-Type";

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "defaultAnswer:\n" +
                       "  headers:\n" +
                       "    " + ACCEPT + ": " + MediaType.TEXT_PLAIN_VALUE + "\n" +
                       "    " + CONTENT_TYPE + ": " + MediaType.APPLICATION_JSON_VALUE + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        assertEquals(MediaType.TEXT_PLAIN_VALUE, headers.getFirst(ACCEPT));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
    }

    @Test
    public void correctRemoveCookieForSimpleGetRequest() throws Exception {
        final String COOKIE = "cookieForRemove";

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "defaultAnswer:\n" +
                       "  removeCookies: [" + COOKIE + "]\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

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

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "defaultAnswer:\n" +
                       "  cookies:\n" +
                       "    - !one.kastordriver.fakerest.bean.Cookie\n" +
                       "      path: " + COOKIE_PATH + "\n" +
                       "      name: " + COOKIE + "\n" +
                       "      value: " + COOKIE_VALUE + "\n" +
                       "      maxAge: 3600\n" +
                       "      secure: true\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.get("Set-Cookie").toString();
        assertTrue(setCookie.contains(String.format("%s=%s;Path=%s", COOKIE, COOKIE_VALUE, COOKIE_PATH)));
        assertTrue(setCookie.contains("Expire"));
        assertTrue(setCookie.contains("Secure"));
    }

    @Test
    public void correctSetHeadersForSimpleGetRequest() throws Exception {
        final String COOKIE = "cookieForAdd";
        final String COOKIE_VALUE = "someValue";
        final String COOKIE_PATH = "somePath";

        String route = "--- !Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer:\n" +
                "  cookies:\n" +
                "    - !one.kastordriver.fakerest.bean.Cookie\n" +
                "      path: " + COOKIE_PATH + "\n" +
                "      name: " + COOKIE + "\n" +
                "      value: " + COOKIE_VALUE + "\n" +
                "      maxAge: 3600\n" +
                "      secure: true\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.get("Set-Cookie").toString();
        assertTrue(setCookie.contains(String.format("%s=%s;Path=%s", COOKIE, COOKIE_VALUE, COOKIE_PATH)));
        assertTrue(setCookie.contains("Expire"));
        assertTrue(setCookie.contains("Secure"));
    }
}
