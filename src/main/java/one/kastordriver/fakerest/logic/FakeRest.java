package one.kastordriver.fakerest.logic;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import one.kastordriver.fakerest.model.Route;
import one.kastordriver.fakerest.exception.RouteInitializationException;
import one.kastordriver.fakerest.exception.UnsupportedHttpMethodException;
import org.springframework.stereotype.Service;
import spark.Spark;
import spark.route.HttpMethod;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Service
@AllArgsConstructor
public class FakeRest {

    private final RoutesReader routesReader;

    private final RouteProcessor routeProcessor;

    private final Map<HttpMethod, BiConsumer<String, spark.Route>> httpMethodsMapping;

    @PreDestroy
    public void destroy() {
        Spark.stop();
    }

    public void start() {
        try {
            routesReader.readRoutes().forEach(this::initRoute);
        } catch (Exception ex) {
            RouteInitializationException rex = new RouteInitializationException("routes initialization error", ex);
            log.error("routes initialization error", rex);
            throw rex;
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
