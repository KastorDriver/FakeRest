package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContentLengthRequestElementTest {

    private static final int CONTENT_LENGTH = 1024;

    @Mock
    private Request request;

    private ContentLengthRequestElement contentLengthRequestElement;

    @BeforeEach
    void setUp() {
        contentLengthRequestElement = new ContentLengthRequestElement();

        when(request.contentLength()).thenReturn(CONTENT_LENGTH);
    }

    @Test
    void shouldReplaceContentLengthRequestElementPrefixWithUnderscore() {
        Binding binding = new Binding();

        String processedConditionExpression = contentLengthRequestElement.processCondition("@contentLength == 1024", request, binding);

        assertThat(processedConditionExpression, equalTo("_contentLength == 1024"));
    }

    @Test
    void shouldBindActualContentLengthToVariable() {
        Binding binding = new Binding();

        contentLengthRequestElement.processCondition("@contentLength == 1024", request, binding);

        assertThat(binding.getProperty("_contentLength"), equalTo(CONTENT_LENGTH));
    }
}
