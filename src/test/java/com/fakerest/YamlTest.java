package com.fakerest;

import com.fakerest.bean.Cookie;
import com.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.junit.Test;
import spark.route.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;

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

        String dump = Yaml.dump(route, true);
        System.out.println(dump);
        Route route2 = Yaml.loadType(dump, Route.class);
    }

    private Cookie prepareCookie(String path, String name, String value, int maxAge, boolean secure) {
        Cookie cookie = new Cookie();
        cookie.setPath(path);
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        return cookie;
    }
}
