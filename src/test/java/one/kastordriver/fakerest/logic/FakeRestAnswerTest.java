package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.config.AppConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;

//TODO fix tests
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FakeRestAnswerTest {

    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";
    private static final String RESPONSE_TEXT = "response text";

    @Spy
    private RoutesReader routesReader;

    @InjectMocks
    @Autowired
    private FakeRest fakeRest;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void after() throws Exception {
        fakeRest.destroy();
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleGetRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "answer:\n" +
                       "  status: " + STATUS_CODE + "\n" +
                       "  body: " + RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
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
                       "answer:\n" +
                       "  headers:\n" +
                       "    " + ACCEPT + ": " + MediaType.TEXT_PLAIN_VALUE + "\n" +
                       "    " + CONTENT_TYPE + ": " + MediaType.APPLICATION_JSON_VALUE + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
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
                       "answer:\n" +
                       "  removeCookies: [" + COOKIE + "]\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.getFirst("Set-Cookie");
        assertTrue(setCookie.contains(COOKIE+"=; Expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0"));
    }

    @Test
    public void correctSetCookieForSimpleGetRequest() throws Exception {
        final String COOKIE = "cookieForAdd";
        final String COOKIE_VALUE = "someValue";
        final String COOKIE_PATH = "somePath";

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "answer:\n" +
                       "  cookies:\n" +
                       "    - !cookie\n" +
                       "      path: " + COOKIE_PATH + "\n" +
                       "      name: " + COOKIE + "\n" +
                       "      value: " + COOKIE_VALUE + "\n" +
                       "      maxAge: 3600\n" +
                       "      secure: true\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.get("Set-Cookie").toString();
        assertTrue(setCookie.contains(String.format("%s=%s; Path=%s", COOKIE, COOKIE_VALUE, COOKIE_PATH)));
        assertTrue(setCookie.contains("Expire"));
        assertTrue(setCookie.contains("Secure"));
    }

    //TODO set headers, not cookies
    @Test
    public void correctSetHeadersForSimpleGetRequest() throws Exception {
        final String COOKIE = "cookieForAdd";
        final String COOKIE_VALUE = "someValue";
        final String COOKIE_PATH = "somePath";

        String route = "---\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "answer:\n" +
                "  cookies:\n" +
                "    - !cookie\n" +
                "      path: " + COOKIE_PATH + "\n" +
                "      name: " + COOKIE + "\n" +
                "      value: " + COOKIE_VALUE + "\n" +
                "      maxAge: 3600\n" +
                "      secure: true\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        HttpHeaders headers = response.getHeaders();
        String setCookie = headers.get("Set-Cookie").toString();
        assertTrue(setCookie.contains(String.format("%s=%s; Path=%s", COOKIE, COOKIE_VALUE, COOKIE_PATH)));
        assertTrue(setCookie.contains("Expire"));
        assertTrue(setCookie.contains("Secure"));
    }

    //TODO fix
//    @Test
//    public void correctStatusAndResponseBodyForSimplePutRequest() throws Exception {
//        final int STATUS_CODE = 200;
//
//        String route = "method: put\n" +
//                "url: " + PATH + "\n" +
//                "answer:\n" +
//                "  status: " + STATUS_CODE + "\n" +
//                "  body: " + RESPONSE_TEXT + "\n";
//
//        doReturn(route).when(settings).loadRoutesFilesIntoString(anyString());
//        fakeRest.start();
//
//        HttpEntity<String> requestHttpEntity = new HttpEntity<>("request body");
//
          //WTF is the error?
//        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.PUT, requestHttpEntity, String.class);
//        assertEquals(STATUS_CODE, response.getStatusCodeValue());
//        assertEquals(RESPONSE_TEXT, response.getBody());
//    }

    @Test
    public void correctStatusAndResponseBodyForSimplePatchRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "method: patch\n" +
                "url: " + PATH + "\n" +
                "answer:\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpEntity<String> requestHttpEntity = new HttpEntity<>("request body");

        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<String> response = restTemplate.exchange(URL + PATH, HttpMethod.PATCH, requestHttpEntity, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());
        assertEquals(RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleHeadRequest() throws Exception {
        final String CONTENT_TYPE = "Content-Type";
        final int STATUS_CODE = 200;

        String route = "method: head\n" +
                "url: " + PATH + "\n" +
                "answer:\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n" +
                "  headers:\n" +
                "    header1: val1\n" +
                "    " + CONTENT_TYPE + ": " + MediaType.APPLICATION_JSON_VALUE + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpEntity<String> requestHttpEntity = new HttpEntity<>("request body");
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.HEAD, requestHttpEntity, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());

        HttpHeaders headers = response.getHeaders();
        assertEquals("val1", headers.getFirst("header1"));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleDeleteRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "method: delete\n" +
                "url: " + PATH + "\n" +
                "answer:\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n" +
                "  headers:\n" +
                "    header1: val1\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpEntity<String> requestHttpEntity = new HttpEntity<>("request body");
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.DELETE, requestHttpEntity, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());

        HttpHeaders headers = response.getHeaders();
        assertEquals("val1", headers.getFirst("header1"));
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleTraceRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "method: trace\n" +
                "url: " + PATH + "\n" +
                "answer:\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n" +
                "  headers:\n" +
                "    header1: val1\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpEntity<String> requestHttpEntity = new HttpEntity<>("request body");
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.TRACE, requestHttpEntity, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());

        HttpHeaders headers = response.getHeaders();
        assertEquals("val1", headers.getFirst("header1"));
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleOptionsRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "method: options\n" +
                "url: " + PATH + "\n" +
                "answer:\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n" +
                "  headers:\n" +
                "    header1: val1\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpEntity<String> requestHttpEntity = new HttpEntity<>("request body");
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.OPTIONS, requestHttpEntity, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());

        HttpHeaders headers = response.getHeaders();
        assertEquals("val1", headers.getFirst("header1"));
    }
}
