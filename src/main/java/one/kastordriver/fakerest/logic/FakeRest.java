package one.kastordriver.fakerest.logic;

import lombok.extern.slf4j.Slf4j;
import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.UnsupportedHttpMethodException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import spark.Spark;
import spark.route.HttpMethod;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class FakeRest {

    @Autowired
    private Settings settings;

    @Autowired
    private RouteProcessor routeProcessor;

    @Autowired
    private Map<HttpMethod, BiConsumer<String, spark.Route>> httpMethodsMapping;

    @PreDestroy
    public void destroy() {
        Spark.stop();
    }

    public void start() throws IOException {
        try {
            settings.loadRoutes().forEach(this::initRoute);
        } catch (Exception ex) {
            log.error("route initialization error", ex);
            throw ex;
        }
    }

    private void initRoute(Route route) {
        HttpMethod httpMethod = HttpMethod.get(route.getMethod());
        processRoute(route, httpMethodsMapping.get(httpMethod));
    }

    private void processRoute(Route route, BiConsumer<String, spark.Route> consumer) {
        if (consumer == null) {
            throw new UnsupportedHttpMethodException("Unsupported http method " + route.getMethod());
        }

        consumer.accept(route.getUrl(), (req, res) -> routeProcessor.process(route, req, res));
    }
}
