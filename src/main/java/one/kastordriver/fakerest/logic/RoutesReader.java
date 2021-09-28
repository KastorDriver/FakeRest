package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.model.Route;
import one.kastordriver.fakerest.exception.RoutesNotFoundException;
import org.ho.yaml.Yaml;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class RoutesReader {

    private static final String YAML_FILE_MASK = "*.yml";

    private final Path routesFilePath;
    private final Path routesDirPath;

    public RoutesReader(Path routesFilePath, Path routesDirPath) {
        this.routesFilePath = routesFilePath;
        this.routesDirPath = routesDirPath;
    }

    public List<Route> readRoutes() throws IOException {
        boolean isRouteFileExists = isRouteFileExists(routesFilePath);
        boolean isRoutesDirExists = isRoutesDirExists(routesDirPath);

        if (!isRouteFileExists && !isRoutesDirExists) {
            throw new RoutesNotFoundException(String.format("The \"%s\" file and the \"%s\" directory don't exist!",
                    routesFilePath, routesDirPath));
        }

        List<Route> routes = new ArrayList<>();

        if (isRouteFileExists) {
            routes.addAll(loadRoutesFromRoutesFile(routesFilePath));
        }

        if (isRoutesDirExists) {
            routes.addAll(loadRoutesFromRoutesDir(routesDirPath));
        }

        return routes;
    }

    private boolean isRouteFileExists(Path routesFilePath) {
        return Files.isRegularFile(routesFilePath);
    }

    private boolean isRoutesDirExists(Path routesDirPath) throws IOException {
        return Files.isDirectory(routesDirPath) && hasRoutesDirRoutesFiles(routesDirPath);
    }

    private List<Route> loadRoutesFromRoutesFile(Path routesFilePath) throws IOException {
        List<Route> routes = new ArrayList<>();
        Yaml.loadStreamOfType(routesFilePath.toFile(), Route.class).forEach(route -> routes.add(route));
        return routes;
    }

    private List<Route> loadRoutesFromRoutesDir(Path routesDirPath) throws IOException {
        List<Route> routes = new ArrayList<>();

        Iterator<Path> iterator = getRoutesFilesFromRoutesDir(routesDirPath).iterator();

        while (iterator.hasNext()) {
            Yaml.loadStreamOfType(iterator.next().toFile(), Route.class).forEach(route -> routes.add(route));
        }

        return routes;
    }

    private boolean hasRoutesDirRoutesFiles(Path routesDirPath) throws IOException {
        return getRoutesFilesFromRoutesDir(routesDirPath).iterator().hasNext();
    }

    private DirectoryStream<Path> getRoutesFilesFromRoutesDir(Path routesDirPath) throws IOException {
        return Files.newDirectoryStream(routesDirPath, YAML_FILE_MASK);
    }
}
