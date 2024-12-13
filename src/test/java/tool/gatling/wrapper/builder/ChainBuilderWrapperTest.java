package tool.gatling.wrapper.builder;

import io.gatling.javaapi.core.Session;
import org.junit.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ChainBuilderWrapperTest {
    @Test
    public void shouldReturnEmptyChainBuilderList() {
        ChainBuilderWrapper wrapper = new ChainBuilderWrapper();
        assertTrue(wrapper.build().isEmpty());
    }

    @Test
    public void shouldAddPropertiesToSession() {
        ChainBuilderWrapper wrapper = new ChainBuilderWrapper();
        Properties appProperties = new Properties();
        appProperties.setProperty("key1", "val1");
        appProperties.setProperty("key2", "val2");

        Session mockSession = mock(Session.class);
        when(mockSession.set("key1", "val1")).thenReturn(mockSession);
        when(mockSession.set("key2", "val2")).thenReturn(mockSession);

        wrapper.addPropertiesToSession(appProperties, mockSession);

        verify(mockSession, times(1)).set("key1", "val1");
        verify(mockSession, times(1)).set("key2", "val2");
    }

}