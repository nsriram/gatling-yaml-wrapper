package tool.gatling.wrapper.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RampConfigTest {
    @Test
    public void shouldReturnMaxDuration() {
        RampConfig rampConfig = new RampConfig();
        assertEquals(5, rampConfig.getDurationConfig(5).intValue());
    }

    @Test
    public void shouldReturnConfiguredDuration() {
        int expectedDuration = 1;
        RampConfig rampConfig = new RampConfig(5, expectedDuration);
        assertEquals(expectedDuration, rampConfig.getDurationConfig(5).intValue());
    }
}