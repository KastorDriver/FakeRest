package one.kastordriver.fakerest.logic;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import one.kastordriver.fakerest.exception.RoutesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoutesReaderTest {

    private static final String ROUTES_FILE_NAME = "routes.yml";
    private static final String ROUTES_DIR_NAME = "routes";

    private FileSystem fileSystem;

    @BeforeEach
    void setUp() {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
    }

    @Test
    void whenRoutesFileAndRoutesDirDoNotExistThenThrowRoutesNotFoundExceptions() {
        Path routesFilePath = fileSystem.getPath(ROUTES_FILE_NAME);
        Path routesDirPath = fileSystem.getPath(ROUTES_DIR_NAME);

        RoutesReader routesReader = new RoutesReader(routesFilePath, routesDirPath);

        RoutesNotFoundException ex = assertThrows(RoutesNotFoundException.class, () -> routesReader.loadRoutes());
        assertThat(ex.getMessage(), equalTo(String.format("There isn't \"%s\" file and \"%s\" directory doesn't exists or empty!",
                ROUTES_FILE_NAME, ROUTES_DIR_NAME)));
    }

    @Test
    void name() {

    }
}
