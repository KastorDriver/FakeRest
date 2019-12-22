package one.kastordriver.fakerest;

import one.kastordriver.fakerest.logic.FakeRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private FakeRest fakeRest;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class);
    }

    @Override
    public void run(String... args) throws Exception {
        fakeRest.start();
    }
}
