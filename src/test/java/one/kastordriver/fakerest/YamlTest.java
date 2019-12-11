package one.kastordriver.fakerest;

import one.kastordriver.fakerest.bean.Answer;
import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.bean.Cookie;
import one.kastordriver.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.junit.Ignore;
import org.junit.Test;
import spark.route.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//TODO fix tests
@Ignore
public class YamlTest {

    @Test
    public void testYaml() {
        Route route = new Route();
        route.setUrl("/path1");
        route.setMethod(HttpMethod.get.name());
        route.getAnswer().setBody("resp1");
        route.getAnswer().setHeaders(new HashMap<String, String>() {{
            put("Content-Type", "text/xml");
            put("header22", "value22");
        }});
        route.getAnswer().setCookies(new ArrayList<Cookie>(){{
            add(prepareCookie("path1", "c1", "v1", -1, false));
            add(prepareCookie("", "c2", "v2", -1, true));
        }});
        route.getAnswer().setRemoveCookies(new ArrayList<String>(){{
            add("cookie1");
            add("cookie2");
        }});

        Condition condition = new Condition();
        condition.setAnswer(new Answer());
        condition.setCondition("new condition");
        route.setConditions(Arrays.asList(condition));

        String dump = Yaml.dump(route, true);
        System.out.println(dump);
    }

    private Cookie prepareCookie(String path, String name, String value, int maxAge, boolean secure) {
        return Cookie.builder().path(path).name(name).value(value).maxAge(maxAge).secure(secure).build();
    }
}
