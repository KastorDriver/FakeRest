package one.kastordriver.fakerest.logic;

import lombok.Value;
import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.RoutesNotFoundException;
import org.ho.yaml.Yaml;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Value
@Component
public class RoutesReader {

    private static final String ROUTE_FILE_MASK = "*.yml";

    private Path routesFilePath;
    private Path routesDirPath;

    public RoutesReader(Path routesFilePath, Path routesDirPath) {
        this.routesFilePath = routesFilePath;
        this.routesDirPath = routesDirPath;
    }

    //TODO rename to readRoutes ?
    public List<Route> loadRoutes() throws IOException {
        boolean isRouteFileExists = isRouteFileExists();
        boolean isRoutesDirExists = isRoutesDirExists();

        if (!isRouteFileExists && !isRoutesDirExists) {
            throw new RoutesNotFoundException(String.format("There isn't \"%s\" file and \"%s\" directory doesn't exists or empty!",
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
        return Files.exists(routesFilePath) && Files.isRegularFile(routesFilePath);
    }

    private boolean isRoutesDirExists() throws IOException {
        return Files.exists(routesDirPath) && Files.isDirectory(routesDirPath) && hasRoutesDirRoutesFiles();
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
        return Files.newDirectoryStream(routesDirPath, ROUTE_FILE_MASK);
    }
}
