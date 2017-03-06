package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.UnsupportedHttpMethodException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Spark;
import spark.route.HttpMethod;

import java.io.IOException;

@Component
public class FakeRest implements DisposableBean {
    private final Logger LOGGER = LoggerFactory.getLogger(FakeRest.class);

    @Autowired
    private Settings settings;

    @Autowired
    private RouteProcessor routeProcessor;

    @Override
    public void destroy() throws Exception {
        Spark.stop();
    }

    public void start() throws IOException {
        try {
            settings.loadRoutesFromFiles().forEach(route -> initRoute(route));
        } catch (Exception ex) {
            LOGGER.error("route initialization error", ex);
            throw ex;
        }
    }

    private void initRoute(Route route) {
        HttpMethod httpMethod = HttpMethod.get(route.getMethod());

        if (HttpMethod.unsupported.equals(httpMethod)) {
            throw new UnsupportedHttpMethodException("Unsupported http method " + route.getMethod());
        }

        //TODO refactor
        switch (httpMethod) {
            case get:
                Spark.get(route.getUrl(), (req, res) -> routeProcessor.process(route, req, res));
                break;
            case post:
                Spark.post(route.getUrl(), (req, res) -> routeProcessor.process(route, req, res));
                break;
        }
    }
}
