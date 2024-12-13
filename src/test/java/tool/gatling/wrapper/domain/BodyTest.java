package tool.gatling.wrapper.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BodyTest {
    @Test
    public void shouldCheckForKeysPresenceInHttpBody() {
        List<String> keys = Arrays.asList("key 1", "key 2");
        Body body = new Body("testRequestTemplate.json", keys);
        assertTrue(body.hasKeys());
    }

    @Test
    public void shouldCheckForKeysNullInHttpBody() {
        Body body = new Body("testRequestTemplate.json", null);
        assertFalse(body.hasKeys());
    }

    @Test
    public void shouldCheckForKeysEmptyInHttpBody() {
        Body body = new Body("testRequestTemplate.json", new ArrayList<>());
        assertFalse(body.hasKeys());
    }

    @Test
    public void shouldFormatKeysToGatlingFormat() {
        List<String> keys = Arrays.asList("key 1", "key-2");
        List<String> expected = Arrays.asList("#{key 1}", "#{key-2}");
        Body body = new Body("testRequestTemplate.json", keys);
        assertEquals(expected, body.formatELKeys());
    }

    @Test
    public void shouldFormatKeysToGatlingFormatWhenEmpty() {
        Body body = new Body("testRequestTemplate.json", new ArrayList<>());
        assertEquals(new ArrayList<>(), body.formatELKeys());
    }

    @Test
    public void shouldFormatKeysToGatlingFormatWhenNull() {
        Body body = new Body("testRequestTemplate.json", null);
        assertEquals(new ArrayList<>(), body.formatELKeys());
    }
}