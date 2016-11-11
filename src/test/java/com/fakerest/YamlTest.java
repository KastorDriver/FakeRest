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
            add(prepareCookie("c1", "v1", null, -1, "", false, true));
            add(prepareCookie("c2", "v2", null, -1, "", true, false));
        }});

        System.out.println(Yaml.dump(route));
    }

    private Cookie prepareCookie(String name, String value, String domain, int maxAge, String path,
                                 boolean secure, boolean isHttpOnly) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(secure);
        cookie.setHttpOnly(isHttpOnly);
        return cookie;
    }
}
