package one.kastordriver.fakerest;

import org.junit.Ignore;

@Ignore
public class YamlTest {

    /*
    @Test
    public void testYaml() {
        Route route = new Route();
        route.setUrl("/path1");
        route.setMethod(HttpMethod.get.name());
        route.getRouteResponse().setBody("resp1");
        route.getRouteResponse().setHeaders(new HashMap<String, String>() {{
            put("Content-Type", "text/xml");
            put("header22", "value22");
        }});
        route.getRouteResponse().setCookies(new ArrayList<Cookie>(){{
            add(prepareCookie("path1", "c1", "v1", -1, false));
            add(prepareCookie("", "c2", "v2", -1, true));
        }});
        route.getRouteResponse().setRemoveCookies(new ArrayList<String>(){{
            add("cookie1");
            add("cookie2");
        }});

        Condition condition = new Condition();
        condition.setRouteResponse(new RouteResponse());
        condition.setCondition("new condition");
        route.setConditions(Arrays.asList(condition));

        String dump = Yaml.dump(route, true);
        System.out.println(dump);
    }

    private Cookie prepareCookie(String path, String name, String value, int maxAge, boolean secure) {
        return Cookie.builder().path(path).name(name).value(value).maxAge(maxAge).secure(secure).build();
    }
    */
}
