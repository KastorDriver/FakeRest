package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Answer;
import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.exception.RouteInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Spark;
import spark.route.HttpMethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FakeRestTest {

    @Mock
    private RoutesReader routesReader;

    @Mock
    private RouteProcessor routeProcessor;

    @Mock
    private Map<HttpMethod, BiConsumer<String, spark.Route>> httpMethodsMapping;

    private FakeRest fakeRest;

    @BeforeEach
    void setUp() {
        httpMethodsMapping = new HashMap<>();
        httpMethodsMapping.put(HttpMethod.get, Spark::get);
        httpMethodsMapping.put(HttpMethod.post, Spark::post);

        fakeRest = new FakeRest(routesReader, routeProcessor, httpMethodsMapping);
    }

    @Test
    void whenFailedToInitializeRouteThenThrowRouteInitializationException() throws IOException {
        when(routesReader.readRoutes()).thenThrow(new RuntimeException("Unexpected exception"));

        RouteInitializationException ex = assertThrows(RouteInitializationException.class, () -> fakeRest.start());

        assertThat(ex.getMessage(), equalTo("routes initialization error"));
        assertThat(ex.getCause().getMessage(), equalTo("Unexpected exception"));
    }

    @Test
    void shouldInitializeAllRoutes() throws IOException {
        when(routesReader.readRoutes()).thenReturn(Arrays.asList(
                Route.builder()
                        .method("get")
                        .url("/simple-path")
                        .answer(Answer.builder()
                                .status(200)
                                .body("Hello world!")
                                .build())
                        .build(),
                Route.builder()
                        .method("post")
                        .url("/another-path")
                        .answer(Answer.builder()
                                .status(200)
                                .body("Another response")
                                .build())
                        .build())
        );


    }
}
