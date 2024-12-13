package tool.gatling.wrapper.config;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AppConfigTest {
    @Test
    public void shouldLoadPropertiesFromFile() {
        Properties properties = AppConfig.create("src/test/resources/dev.properties");
        assertEquals(3, properties.size());
        assertEquals("val1", properties.getProperty("key1"));
        assertEquals("val2", properties.getProperty("key2"));
        assertEquals("val3", properties.getProperty("key3"));
    }

    @Test
    public void shouldReturnEmptyPropertiesWhenFileDoesNotExist() {
        assertThrows(IllegalStateException.class, () -> {
            AppConfig.create("src/test/resources/nonexistent.properties");
        });
    }
}