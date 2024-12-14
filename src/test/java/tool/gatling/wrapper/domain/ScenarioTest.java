package tool.gatling.wrapper.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScenarioTest {
    @Test
    public void shouldCheckIfRampConfigIsMissingInScenario() {
        Scenario scenario = new Scenario();
        assertFalse(scenario.hasRampConfig());
    }

    @Test
    public void shouldCheckIfScenarioHasRampConfig() {
        Scenario scenario = new Scenario();
        scenario.setRampConfig(new RampConfig());
        assertTrue(scenario.hasRampConfig());
    }
}