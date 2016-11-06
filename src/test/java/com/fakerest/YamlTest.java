package com.fakerest;

import com.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.junit.Test;
import spark.route.HttpMethod;

public class YamlTest {

    @Test
    public void testYaml() {
        Route route = new Route();
        route.setPath("/path1");
        route.setType(HttpMethod.get.name());
        route.getAnswer().setBody("resp1");

        System.out.println(Yaml.dump(route));
    }
}
