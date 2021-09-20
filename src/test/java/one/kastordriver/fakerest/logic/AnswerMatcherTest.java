package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Answer;
import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.bean.Route;
import one.kastordriver.fakerest.logic.request.HeaderRequestElement;
import one.kastordriver.fakerest.logic.request.IpRequestElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerMatcherTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final String JOHN_WICK = "John Wick";

    private AnswerMatcher answerMatcher;

    @Mock
    private Request request;

    @Test
    void ifRouteDoesNotHaveConditionsThenReturnDefaultAnswer() {
        answerMatcher = new AnswerMatcher(asList());

        Answer defaultAnswer = Answer.builder()
                .body("Default answer")
                .build();

        Route route = Route.builder()
                .answer(defaultAnswer)
                .conditions(emptyList())
                .build();

        assertThat(answerMatcher.findAppropriateAnswer(route, request), equalTo(defaultAnswer));
    }

    @Test
    void shouldReturnAnswerForMatchedCondition() {
        answerMatcher = new AnswerMatcher(asList(new IpRequestElement()));

        when(request.ip()).thenReturn(LOCALHOST);

        Answer defaultAnswer = Answer.builder()
                .body("Default answer")
                .build();

        Answer conditionAnswer = Answer.builder()
                .body("Condition answer")
                .build();

        Route route = Route.builder()
                .answer(defaultAnswer)
                .conditions(asList(new Condition(String.format("@ip == \"%s\"", LOCALHOST), conditionAnswer)))
                .build();

        assertThat(answerMatcher.findAppropriateAnswer(route, request), equalTo(conditionAnswer));
    }

    @Test
    void ifMoreThanOneConditionsAreMatchedThenReturnAnswerForTheFirstMatchedCondition() {
        answerMatcher = new AnswerMatcher(asList(new IpRequestElement(), new HeaderRequestElement()));

        when(request.headers("name")).thenReturn(JOHN_WICK);

        Answer defaultAnswer = Answer.builder()
                .body("Default answer")
                .build();

        Answer headerConditionAnswer = Answer.builder()
                .body("header condition answer")
                .build();

        Answer ipConditionAnswer = Answer.builder()
                .body("ip condition answer")
                .build();

        Route route = Route.builder()
                .answer(defaultAnswer)
                .conditions(asList(
                        new Condition(String.format("@header(name) == \"%s\"", JOHN_WICK), headerConditionAnswer),
                        new Condition(String.format("@ip == \"%s\"", LOCALHOST), ipConditionAnswer)))
                .build();

        assertThat(answerMatcher.findAppropriateAnswer(route, request), equalTo(headerConditionAnswer));
    }

    @Test
    void ifNoneOfConditionsAreMatchedThenReturnDefaultAnswer() {
        answerMatcher = new AnswerMatcher(asList(new IpRequestElement(), new HeaderRequestElement()));

        when(request.headers("name")).thenReturn("unexpected value");
        when(request.ip()).thenReturn("unexpected value");

        Answer defaultAnswer = Answer.builder()
                .body("Default answer")
                .build();

        Answer headerConditionAnswer = Answer.builder()
                .body("header condition answer")
                .build();

        Answer ipConditionAnswer = Answer.builder()
                .body("ip condition answer")
                .build();

        Route route = Route.builder()
                .answer(defaultAnswer)
                .conditions(asList(
                        new Condition(String.format("@header(name) == \"%s\"", JOHN_WICK), headerConditionAnswer),
                        new Condition(String.format("@ip == \"%s\"", LOCALHOST), ipConditionAnswer)))
                .build();

        assertThat(answerMatcher.findAppropriateAnswer(route, request), equalTo(defaultAnswer));
    }
}
