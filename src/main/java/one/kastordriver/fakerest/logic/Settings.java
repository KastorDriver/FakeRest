package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.RouteFileNotFoundException;
import org.ho.yaml.Yaml;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class Settings {

    @Value("${port:4567}")
    private int port;

    private static final String ROUTES_FILE_NAME = "routes.yml";
    private static final String ROUTES_DIR_NAME = "routes";
    private static final String ROUTE_FILE_MASK = "*.yml";

    public List<Route> loadRoutes() throws IOException {
        boolean isRouteFileExists = isRouteFileExists();
        boolean isRoutesDirExists = isRoutesDirExists();

        if (!isRouteFileExists && !isRoutesDirExists) {
            throw new RouteFileNotFoundException(String.format("There isn't \"%s\" file and \"%s\" directory doesn't exists or empty!",
                    ROUTES_FILE_NAME, ROUTES_DIR_NAME));
        }

        List<Route> routes = new ArrayList<>();

        if (isRouteFileExists) {
            routes.addAll(loadRoutesFromRoutesFile());
        }

        if (isRoutesDirExists) {
            routes.addAll(loadRoutesFromRouteDir());
        }

        return routes;
    }

    private boolean isRouteFileExists() {
        Path routesFile = Paths.get(ROUTES_FILE_NAME);
        return Files.exists(routesFile) && Files.isRegularFile(routesFile);
    }

    private boolean isRoutesDirExists() throws IOException {
        Path routesDir = Paths.get(ROUTES_DIR_NAME);
        return Files.exists(routesDir) && Files.isDirectory(routesDir) && hasRoutesDirRoutesFiles();
    }

    private List<Route> loadRoutesFromRoutesFile() throws IOException {
        List<Route> routes = new ArrayList<>();
        Yaml.loadStreamOfType(loadRoutesFilesIntoString(ROUTES_FILE_NAME), Route.class).forEach(route -> routes.add(route));
        return routes;
    }

    String loadRoutesFilesIntoString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
    }

    private List<Route> loadRoutesFromRouteDir() throws IOException {
        List<Route> routes = new ArrayList<>();

        Iterator<Path> iterator = getRoutesFilesFromRoutesDir().iterator();

        while (iterator.hasNext()) {
            Yaml.loadStreamOfType(iterator.next().toFile(), Route.class).forEach(route -> routes.add(route));
        }

        return routes;
    }

    private boolean hasRoutesDirRoutesFiles() throws IOException {
        return getRoutesFilesFromRoutesDir().iterator().hasNext();
    }

    private DirectoryStream<Path> getRoutesFilesFromRoutesDir() throws IOException {
        Path routesDir = Paths.get(ROUTES_DIR_NAME);
        return Files.newDirectoryStream(routesDir, ROUTE_FILE_MASK);
    }
}
