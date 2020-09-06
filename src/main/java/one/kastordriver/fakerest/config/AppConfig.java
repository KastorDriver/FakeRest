package one.kastordriver.fakerest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.PathResource;
import spark.Route;
import spark.Spark;
import spark.route.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Configuration
@ComponentScan(basePackages = "one.kastordriver.fakerest")
public class AppConfig {

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
}
