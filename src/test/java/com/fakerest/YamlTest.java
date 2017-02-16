package com.fakerest;

import com.fakerest.bean.Answer;
import com.fakerest.bean.Condition;
import com.fakerest.bean.Cookie;
import com.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.junit.Test;
import spark.route.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class YamlTest {

    @Test
    public void testYaml() {
        Route route = new Route();
        route.setUrl("/path1");
        route.setMethod(HttpMethod.get.name());
        route.getDefaultAnswer().setBody("resp1");
        route.getDefaultAnswer().setHeaders(new HashMap<String, String>() {{
            put("Content-Type", "text/xml");
            put("header22", "value22");
        }});
        route.getDefaultAnswer().setCookies(new ArrayList<Cookie>(){{
            add(prepareCookie("path1", "c1", "v1", -1, false));
            add(prepareCookie("", "c2", "v2", -1, true));
        }});
        route.getDefaultAnswer().setRemoveCookies(new ArrayList<String>(){{
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
