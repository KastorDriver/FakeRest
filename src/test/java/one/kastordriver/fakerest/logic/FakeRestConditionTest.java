package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.config.AppConfig;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//TODO fix tests
@Ignore
@Disabled("Fix tests!")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FakeRestConditionTest {

    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";

    @Spy
    private RoutesReader routesReader;

    @InjectMocks
    @Autowired
    private FakeRest fakeRest;

    /*
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void after() throws Exception {
        fakeRest.destroy();
    }

    @Test
    public void correctAnswerForIpCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @ip == \"127.0.0.1\"\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerForPortCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @port == 4567\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerForContentLengthCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;
        final String REQUEST_BODY = "requestBody";

        String route = "method: post\n" +
                       "url: " + PATH + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @contentLength == " + REQUEST_BODY.length() + "\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().postForEntity(URL + PATH, REQUEST_BODY, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerForCookieCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @cookie(someCookie) == \"cookieValue\" && @cookie(someCookie2) == \"cookieValue2\"\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "someCookie=cookieValue");
        headers.add("Cookie", "someCookie2=cookieValue2");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.GET, httpEntity, String.class);

        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerForHeaderCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @header(Accept) == \"application/json\"\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.GET, httpEntity, String.class);

        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerForQueryParamCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "method: get\n" +
                       "url: " + PATH + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @queryParam(par1) == \"val1\" && @queryParam(par2) == \"val2\"\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH + "?par1=val1&par2=val2", String.class);

        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerForPathParamCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "method: get\n" +
                       "url: " + PATH + "/:pathParam1/:secondPathParam" + "\n" +
                       "routeResponse: !RouteResponse\n" +
                       "  status: 200\n" +
                       "  body: response text\n" +
                       "conditions:\n" +
                       "  - !condition\n" +
                       "    condition: @pathParam(pathParam1) == \"pathVal1\" && @pathParam(secondPathParam) == \"pathVal2\"" + "\n" +
                       "    routeResponse:\n" +
                       "      status: " + CONDITION_STATUS_CODE + "\n" +
                       "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(routesReader).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH + "/pathVal1/pathVal2", String.class);

        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }
    */
}
