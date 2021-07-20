package one.kastordriver.fakerest.logic;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import one.kastordriver.fakerest.exception.RoutesNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public class RoutesReaderTest {

    private FileSystem fileSystem;

    @BeforeEach
    void setUp() {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
    }

    @Test
    void whenRoutesFileAndRoutesDirDoNotExistThenThrowRoutesNotFoundExceptions() {
        Path routesFilePath = fileSystem.getPath("routes.yml");
        Path routesDirPath = fileSystem.getPath("routes");

        RoutesReader routesReader = new RoutesReader(routesFilePath, routesDirPath);

        Assertions.assertThrows(RoutesNotFoundException.class, () -> routesReader.loadRoutes());
    }
}
