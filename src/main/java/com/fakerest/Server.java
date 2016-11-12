package com.fakerest;

import com.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import spark.Spark;
import spark.route.HttpMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

public class Server {
    private static final String ROUTES_CONFIG_FILE_NAME = "routes.yaml";

    public static void main(String[] args) throws FileNotFoundException {
        Yaml.loadStreamOfType(new File(ROUTES_CONFIG_FILE_NAME), Route.class)
                .forEach(route -> initRoute(route));
    }

    private static void initRoute(Route route) {
        if (HttpMethod.unsupported.equals(HttpMethod.get(route.getMethod()))) {
            throw new RuntimeException("Unsupported http method");
        }

        try {
            Method method = Spark.class.getMethod(route.getMethod(), String.class, spark.Route.class);
            method.invoke(null, route.getUrl(), route);
        } catch (Exception ex) {
            throw new RuntimeException("init route error", ex);
        }
    }
}
