package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.model.RouteResponse;
import one.kastordriver.fakerest.model.Route;
import one.kastordriver.fakerest.exception.RoutesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoutesReaderTest {

    private static final String ROUTES_FILE_NAME = "routes.yml";
    private static final String ROUTES_DIR_NAME = "routes";

    private static final String NON_EXISTENT_ROUTES_FILE_NAME = "nonExistentRoutes.yml";
    private static final String NONEXISTENT_ROUTES_DIR_NAME = "nonExistentRoutes";

    private Path routesFile;
    private Path routesDir;

    private Path nonExistentRoutesFile;
    private Path nonExistentRoutesDir;

    @BeforeEach
    void setUp() throws IOException {
        routesFile = new ClassPathResource(ROUTES_FILE_NAME).getFile().toPath();
        routesDir = Paths.get("src", "test", "resources", ROUTES_DIR_NAME);

        nonExistentRoutesFile = Paths.get(NON_EXISTENT_ROUTES_FILE_NAME);
        nonExistentRoutesDir = Paths.get(NONEXISTENT_ROUTES_DIR_NAME);
    }

    @Test
    void whenRoutesFileAndRoutesDirDoNotExistThenThrowRoutesNotFoundExceptions() {
        RoutesReader routesReader = new RoutesReader(nonExistentRoutesFile, nonExistentRoutesDir);

        RoutesNotFoundException ex = assertThrows(RoutesNotFoundException.class, () -> routesReader.readRoutes());
        assertThat(ex.getMessage(), equalTo(String.format("The \"%s\" file and the \"%s\" directory don't exist!",
                NON_EXISTENT_ROUTES_FILE_NAME, NONEXISTENT_ROUTES_DIR_NAME)));
    }

    @Test
    void shouldReadAllRoutesFromRoutesFile() throws IOException {
        RoutesReader routesReader = new RoutesReader(routesFile, nonExistentRoutesDir);
        List<Route> routes = routesReader.readRoutes();
        assertThat(routes, containsInAnyOrder(
                Route.builder()
                        .method("get")
                        .url("/simple-path")
                        .conditions(emptyList())
                        .response(RouteResponse.builder()
                                .status(200)
                                .body("Hello world!")
                                .headers(emptyMap())
                                .cookies(emptyList())
                                .removeCookies(emptyList())
                                .build())
                        .build(),
                Route.builder()
                        .method("post")
                        .url("/another-path")
                        .conditions(emptyList())
                        .response(RouteResponse.builder()
                                .status(200)
                                .body("Another response")
                                .headers(emptyMap())
                                .cookies(emptyList())
                                .removeCookies(emptyList())
                                .build())
                        .build())
        );
    }

    @Test
    void shouldReadAllRoutesFromRoutesDirectory() throws IOException {
        RoutesReader routesReader = new RoutesReader(nonExistentRoutesFile, routesDir);
        List<Route> routes = routesReader.readRoutes();
        assertThat(routes, containsInAnyOrder(Route.builder()
                        .method("get")
                        .url("/simple-path")
                        .conditions(emptyList())
                        .response(RouteResponse.builder()
                                .status(200)
                                .body("Hello world!")
                                .headers(emptyMap())
                                .cookies(emptyList())
                                .removeCookies(emptyList())
                                .build())
                        .build(),
                Route.builder()
                        .method("post")
                        .url("/another-path")
                        .conditions(emptyList())
                        .response(RouteResponse.builder()
                                .status(200)
                                .body("Another response")
                                .headers(emptyMap())
                                .cookies(emptyList())
                                .removeCookies(emptyList())
                                .build())
                        .build(),
                Route.builder()
                        .method("head")
                        .url("/third-path")
                        .conditions(emptyList())
                        .response(RouteResponse.builder()
                                .status(200)
                                .body("Third response!")
                                .headers(emptyMap())
                                .cookies(emptyList())
                                .removeCookies(emptyList())
                                .build())
                        .build())
        );
    }
}
