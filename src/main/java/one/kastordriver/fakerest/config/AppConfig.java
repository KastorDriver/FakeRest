package one.kastordriver.fakerest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.PathResource;

@Configuration
@ComponentScan(basePackages = "one.kastordriver.fakerest")
public class AppConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setLocalOverride(false);
        pspc.setIgnoreResourceNotFound(true);
        pspc.setLocation(new PathResource("config.properties"));
        return pspc;
    }
}
