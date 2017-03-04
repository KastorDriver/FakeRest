package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.InitializeRouteException;
import one.kastordriver.fakerest.exception.UnsupportedHttpMethodException;
import org.ho.yaml.Yaml;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import spark.Spark;
import spark.route.HttpMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FakeRest implements InitializingBean {
    private static final String ROUTES_CONFIG_FILE_NAME = "routes.yaml";

    void start() throws IOException {
        loadRoutesFromFiles().forEach(route -> initRoute(route));
    }

    private List<Route> loadRoutesFromFiles() throws IOException {
        List<Route> routes = new ArrayList<>();
        Yaml.loadStreamOfType(loadRoutesFilesIntoString(ROUTES_CONFIG_FILE_NAME), Route.class).forEach(route -> routes.add(route));
        return routes;
    }

    String loadRoutesFilesIntoString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }

    private void initRoute(Route route) {
        if (HttpMethod.unsupported.equals(HttpMethod.get(route.getMethod()))) {
            throw new UnsupportedHttpMethodException("Unsupported http method " + route.getMethod());
        }

        try {
            Method method = Spark.class.getMethod(route.getMethod(), String.class, spark.Route.class);
            method.invoke(null, route.getUrl(), route);
        } catch (Exception ex) {
            throw new InitializeRouteException("init route error", ex);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
