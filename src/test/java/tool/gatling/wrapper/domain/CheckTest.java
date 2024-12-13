package tool.gatling.wrapper.domain;

import io.gatling.javaapi.core.CheckBuilder;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import tool.gatling.wrapper.builder.ResponseCheckBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CheckTest {
    @Test
    public void shouldCheckForJmesPathChecksPresence() {
        Map<String, String> jmesPathChecks = Map.of("responseStatus.status", "0");
        Check check = new Check();
        check.setJmesPathChecks(jmesPathChecks);
        assertTrue(check.hasJmesPathChecks());
    }

    @Test
    public void shouldCheckForJmesPathChecksAbsence() {
        Check check = new Check();
        assertFalse(check.hasJmesPathChecks());
    }

    @Test
    public void shouldCheckForJmesPathChecksEmpty() {
        Check check = new Check();
        check.setJmesPathChecks(Map.of());
        assertFalse(check.hasJmesPathChecks());
    }

    @Test
    public void shouldCheckForJmesPathPresence() {
        List<String> jmesPaths = asList("responseStatus.status", "response.something");
        Check check = new Check();
        check.setJmesPaths(jmesPaths);
        assertTrue(check.hasJmesPath());
    }

    @Test
    public void shouldCheckForJmesPathAbsence() {
        Check check = new Check();
        assertFalse(check.hasJmesPath());
    }

    @Test
    public void shouldCheckForJmesPathEmpty() {
        Check check = new Check();
        check.setJmesPaths(List.of());
        assertFalse(check.hasJmesPath());
    }

    @Test
    public void shouldReturnEmptyCheckBuilderList() {
        Check check = new Check();
        assertTrue(check.checkBuilders().isEmpty());
    }

    @Test
    public void shouldReturnCheckBuilderForResponseStatus() {
        Check check = new Check();
        int statusCode = 200;
        check.setStatus(statusCode);

        try (MockedConstruction<ResponseCheckBuilder> mocked = mockResponseCheckBuilder(CheckTest::isAddStatus)) {
            List<CheckBuilder> actualCheckBuilders = check.checkBuilders();

            verify(mocked.constructed().get(0)).addStatus(statusCode);
            verify(mocked.constructed().get(0)).build();

            assertEquals(2, actualCheckBuilders.size());
        }
    }

    @Test
    public void shouldReturnCheckBuilderForResponseTime() {
        Check check = new Check();
        Integer responseTime = 1000;
        check.setResponseTimeInMillis(responseTime);

        try (MockedConstruction<ResponseCheckBuilder> mockedBuilder = mockResponseCheckBuilder(CheckTest::isAddResponseTime)) {
            List<CheckBuilder> actualCheckBuilders = check.checkBuilders();

            verify(mockedBuilder.constructed().get(0)).addResponseTime(responseTime);
            verify(mockedBuilder.constructed().get(0)).build();

            assertEquals(2, actualCheckBuilders.size());
        }
    }

    @Test
    public void shouldReturnCheckBuilderForJmesPath() {
        Check check = new Check();
        List<String> jmesPaths = asList("responseStatus.status", "response.something");
        check.setJmesPaths(jmesPaths);

        try (MockedConstruction<ResponseCheckBuilder> mocked = mockResponseCheckBuilder(CheckTest::isAddJmesPath)) {
            List<CheckBuilder> actualCheckBuilders = check.checkBuilders();

            verify(mocked.constructed().get(0)).addJmesPath(jmesPaths);
            verify(mocked.constructed().get(0)).build();

            assertEquals(2, actualCheckBuilders.size());
        }
    }

    @Test
    public void shouldReturnCheckBuilderForJmesPathChecks() {
        Check check = new Check();
        Map<String, String> jmesPathChecks = Map.of("responseStatus.status", "0");
        check.setJmesPathChecks(jmesPathChecks);

        try (MockedConstruction<ResponseCheckBuilder> mocked = mockResponseCheckBuilder(CheckTest::isAddJmesPathChecks)) {
            List<CheckBuilder> actualCheckBuilders = check.checkBuilders();

            verify(mocked.constructed().get(0)).addJmesPathChecks(jmesPathChecks);
            verify(mocked.constructed().get(0)).build();

            assertEquals(2, actualCheckBuilders.size());
        }
    }

    private MockedConstruction<ResponseCheckBuilder> mockResponseCheckBuilder(Function<InvocationOnMock, Boolean> isMethodCalled) {
        return Mockito.mockConstructionWithAnswer(ResponseCheckBuilder.class,
                invocation -> isMethodCalled.apply(invocation) ? null : asList(mock(CheckBuilder.class),mock(CheckBuilder.class))
        );
    }

    private static boolean isAddStatus(InvocationOnMock invocation) {
        return invocation.getMethod().getName().equals("addStatus");
    }

    private static boolean isAddResponseTime(InvocationOnMock invocation) {
        boolean bool = invocation.getMethod().getName().equals("addResponseTime");
        return invocation.getMethod().getName().equals("addResponseTime");
    }

    private static boolean isAddJmesPath(InvocationOnMock invocation) {
        return invocation.getMethod().getName().equals("addJmesPath");
    }

    private static boolean isAddJmesPathChecks(InvocationOnMock invocation) {
        return invocation.getMethod().getName().equals("addJmesPathChecks");
    }
}