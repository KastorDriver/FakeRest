package com.fakerest;

import com.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.junit.Test;
import spark.route.HttpMethod;

import java.util.HashMap;

public class YamlTest {

    @Test
    public void testYaml() {
        Route route = new Route();
        route.setPath("/path1");
        route.setMethod(HttpMethod.get.name());
        route.getAnswer().setBody("resp1");
        route.getAnswer().setHeaders(new HashMap<String, String>() {{
            put("Content-Type", "text/xml");
            put("header22", "value22");
        }});

        System.out.println(Yaml.dump(route));
    }
}
