package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Route;
import org.ho.yaml.Yaml;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class Settings {

    @Value("${port:4567}")
    private int port;

    private static final String ROUTES_CONFIG_FILE_NAME = "routes.yaml";

    public List<Route> loadRoutesFromFiles() throws IOException {
        List<Route> routes = new ArrayList<>();
        Yaml.loadStreamOfType(loadRoutesFilesIntoString(ROUTES_CONFIG_FILE_NAME), Route.class).forEach(route -> routes.add(route));
        return routes;
    }

    String loadRoutesFilesIntoString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }
}
