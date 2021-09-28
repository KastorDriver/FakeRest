package one.kastordriver.fakerest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.PathResource;
import spark.Route;
import spark.Spark;
import spark.route.HttpMethod;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Configuration
public class AppConfig {

    private static final String ROUTES_FILE_DEFAULT_PATH = "routes.yml";
    private static final String ROUTES_DIR_DEFAULT_PATH = "routes";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocalOverride(false);
        pspc.setIgnoreResourceNotFound(true);
        pspc.setLocation(new PathResource("config.properties"));
        return pspc;
    }

    @Bean
    public Map<HttpMethod, BiConsumer<String, Route>> httpMethodsMapping() {
        Map<HttpMethod, BiConsumer<String, Route>> mapping = new HashMap<>();
        mapping.put(HttpMethod.get, Spark::get);
        mapping.put(HttpMethod.post, Spark::post);
        mapping.put(HttpMethod.put, Spark::put);
        mapping.put(HttpMethod.patch, Spark::patch);
        mapping.put(HttpMethod.head, Spark::head);
        mapping.put(HttpMethod.delete, Spark::delete);
        mapping.put(HttpMethod.trace, Spark::trace);
        mapping.put(HttpMethod.options, Spark::options);
        return mapping;
    }

    @Bean
    public Path routesFilePath(@Value("${routesFilePath:" + ROUTES_FILE_DEFAULT_PATH + "}") Path routesFilePath) {
        return routesFilePath;
    }

    @Bean
    public Path routesDirPath(@Value("${routesDirPath:" + ROUTES_DIR_DEFAULT_PATH + "}") Path routesDirPath) {
        return routesDirPath;
    }
}
