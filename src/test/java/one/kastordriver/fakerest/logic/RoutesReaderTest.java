package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Answer;
import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.RoutesNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoutesReaderTest {

    private static final String ROUTES_FILE_NAME = "routes.yml";
    private static final String ROUTES_DIR_NAME = "routes";

    private static final String NON_EXISTENT_ROUTES_FILE_NAME = "nonExistentRoutes.yml";
    private static final String NONEXISTENT_ROUTES_DIR_NAME = "nonExistentRoutes";

    private Path routesFile;
    private Path routesDir;

    @BeforeEach
    void setUp() throws IOException {
        routesFile = new ClassPathResource(ROUTES_FILE_NAME).getFile().toPath();
        routesDir = Paths.get("src", "test", "resources", ROUTES_DIR_NAME);
    }

    @Test
    void whenRoutesFileAndRoutesDirDoNotExistThenThrowRoutesNotFoundExceptions() {
        RoutesReader routesReader = new RoutesReader(Paths.get(NON_EXISTENT_ROUTES_FILE_NAME), Paths.get(NONEXISTENT_ROUTES_DIR_NAME));

        RoutesNotFoundException ex = assertThrows(RoutesNotFoundException.class, () -> routesReader.loadRoutes());
        assertThat(ex.getMessage(), equalTo(String.format("There isn't \"%s\" file and \"%s\" directory doesn't exists or empty!",
                NON_EXISTENT_ROUTES_FILE_NAME, NONEXISTENT_ROUTES_DIR_NAME)));
    }

    @Test
    void shouldReadAllRoutesFromRoutesFile() throws IOException {
        RoutesReader routesReader = new RoutesReader(routesFile, routesDir);
        List<Route> routes = routesReader.loadRoutes();
        contains(routes,
                Route.builder()
                        .method("get")
                        .url("/simple-path")
                        .answer(Answer.builder().body("Hello world!").build())
                        .build(),
                Route.builder()
                        .method("post")
                        .url("/another-path")
                        .answer(Answer.builder().body("Another response").build())
                        .build()
        );
    }
}
