package one.kastordriver.fakerest;

import one.kastordriver.fakerest.config.AppConfig;
import one.kastordriver.fakerest.logic.FakeRest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class App {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        FakeRest fakeRest = context.getBean(FakeRest.class);
        fakeRest.start();
    }
}
