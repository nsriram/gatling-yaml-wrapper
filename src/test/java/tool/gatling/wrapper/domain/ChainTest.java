package tool.gatling.wrapper.domain;

import io.gatling.javaapi.core.CheckBuilder;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import tool.gatling.wrapper.builder.SaveResponseCheckBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChainTest {
    @Test
    public void shouldCheckForHeadersPresence() {
        Map<String, String> headers = Map.of("Authorization", "Bearer token",
                "Content-Type", "application/json");
        Chain chain = new Chain();
        chain.setHeaders(headers);
        assertTrue(chain.hasHeaders());
    }

    @Test
    public void shouldCheckForHeadersAbsence() {
        Chain chain = new Chain();
        assertFalse(chain.hasHeaders());
    }

    @Test
    public void shouldCheckForHeadersEmpty() {
        Chain chain = new Chain();
        chain.setHeaders(Map.of());
        assertFalse(chain.hasHeaders());
    }

    @Test
    public void shouldCheckForSignatureFieldsPresence() {
        List<String> signatureFields = Arrays.asList("accountNumber", "amount", "currency");
        Chain chain = new Chain();
        chain.setSignatureFields(signatureFields);
        assertTrue(chain.hasSignatureFields());
    }

    @Test
    public void shouldCheckForSignatureFieldsAbsence() {
        Chain chain = new Chain();
        assertFalse(chain.hasSignatureFields());
    }

    @Test
    public void shouldCheckForSignatureFieldsEmpty() {
        Chain chain = new Chain();
        chain.setSignatureFields(Arrays.asList());
        assertFalse(chain.hasSignatureFields());
    }

    @Test
    public void shouldReturnEmptyCheckBuilderList() {
        Chain chain = new Chain();
        List<CheckBuilder> checkBuilders = chain.checkBuilders();
        assertEquals(0, checkBuilders.size());
    }

    @Test
    public void shouldReturnCheckBuilderList() {
        Chain chain = new Chain();

        Check mockCheck = mock(Check.class);
        List<CheckBuilder> mockCheckBuilders = Arrays.asList(mock(CheckBuilder.class), mock(CheckBuilder.class));
        when(mockCheck.checkBuilders()).thenReturn(mockCheckBuilders);

        chain.setCheck(mockCheck);

        List<CheckBuilder> checkBuilders = chain.checkBuilders();

        assertEquals(mockCheckBuilders.size(), checkBuilders.size());
        assertSame(mockCheckBuilders, checkBuilders);
    }

    @Test
    public void shouldCheckForSaveResponseFieldsPresence() {
        Map<String, String> saveResponseFields = Map.of("data.accessToken", "accessToken",
                "response-field-2", "as-field-2");
        Chain chain = new Chain();
        chain.setSaveResponseFields(saveResponseFields);
        assertTrue(chain.hasSaveResponseFields());
    }

    @Test
    public void shouldCheckForSaveResponseFieldsAbsence() {
        Chain chain = new Chain();
        assertFalse(chain.hasSaveResponseFields());
    }

    @Test
    public void shouldCheckForSaveResponseFieldsEmpty() {
        Chain chain = new Chain();
        chain.setSaveResponseFields(Map.of());
        assertFalse(chain.hasSaveResponseFields());
    }

    @Test
    public void shouldReturnSaveResponseFieldValues() {
        Map<String, String> saveResponseFields = Map.of("data.accessToken", "accessToken",
                "response-field-2", "as-field-2");
        Chain chain = new Chain();
        chain.setSaveResponseFields(saveResponseFields);
        List<String> fieldValues = chain.saveResponseFieldValues();
        assertEquals(saveResponseFields.size(), fieldValues.size());
        assertTrue(fieldValues.containsAll(saveResponseFields.values()));
    }

    @Test
    public void shouldReturnEmptySaveResponseFieldValues() {
        Chain chain = new Chain();
        List<String> fieldValues = chain.saveResponseFieldValues();
        assertEquals(0, fieldValues.size());
    }

    @Test
    public void shouldCheckForQueryParamsPresence() {
        Map<String, String> headers = Map.of("field1", "value1",
                "field2", "value2");
        Chain chain = new Chain();
        chain.setQueryParams(headers);
        assertTrue(chain.hasQueryParams());
    }

    @Test
    public void shouldCheckForQueryParamsAbsence() {
        Chain chain = new Chain();
        assertFalse(chain.hasQueryParams());
    }

    @Test
    public void shouldCheckForQueryParamsEmpty() {
        Chain chain = new Chain();
        chain.setQueryParams(Map.of());
        assertFalse(chain.hasQueryParams());
    }

    @Test
    public void shouldReturnCheckBuilderWithDefaultSavedFields() {
        Chain chain = new Chain();
        List<CheckBuilder> mockDefaultCheckBuilders = Arrays.asList(mock(CheckBuilder.class),
                mock(CheckBuilder.class));

        try (MockedConstruction<SaveResponseCheckBuilder> mocked = Mockito.mockConstructionWithAnswer(SaveResponseCheckBuilder.class,
                invocation -> mockDefaultCheckBuilders)) {
            List<CheckBuilder> checkBuilders = chain.saveResponseFieldsWithDefault();

            verify(mocked.constructed().get(0)).build();
            assertEquals(2, checkBuilders.size());
            assertSame(mockDefaultCheckBuilders, checkBuilders);
        }
    }

    @Test
    public void shouldReturnCheckBuilderWithDefaultAndSavedFields() {
        Chain chain = new Chain();
        Map<String, String> saveResponseFields = Map.of("data.accessToken", "accessToken",
                "response-field-2", "as-field-2");
        chain.setSaveResponseFields(saveResponseFields);

        List<CheckBuilder> mockCheckBuilders = mockCheckBuilders();


        try (MockedConstruction<SaveResponseCheckBuilder> mocked = Mockito.mockConstructionWithAnswer(SaveResponseCheckBuilder.class, invocation -> {
            if (isBuild(invocation)) {
                return mockCheckBuilders;
            }
            return invocation.getMock();

        })) {
            List<CheckBuilder> checkBuilders = chain.saveResponseFieldsWithDefault();

            verify(mocked.constructed().get(0)).addSaveResponseFields(saveResponseFields);
            verify(mocked.constructed().get(0)).build();
            assertEquals(4, checkBuilders.size());
            assertSame(mockCheckBuilders, checkBuilders);
        }
    }

    private static List<CheckBuilder> mockCheckBuilders() {
        List<CheckBuilder> mockCheckBuilders = Arrays.asList(mock(CheckBuilder.class),
                mock(CheckBuilder.class),
                mock(CheckBuilder.class),
                mock(CheckBuilder.class)
        );
        return mockCheckBuilders;
    }

    private static boolean isBuild(InvocationOnMock invocation) {
        return invocation.getMethod().getName().equals("build");
    }
}