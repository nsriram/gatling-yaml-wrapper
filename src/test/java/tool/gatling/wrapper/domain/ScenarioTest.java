package tool.gatling.wrapper.domain;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ScenarioTest {
    @Test
    public void shouldCheckForFeedFilePresence() {
        Map<String, String> feedFile = Map.of("dev", "dev-data.csv");
        Scenario scenario = new Scenario();
        scenario.setFeedFile(feedFile);
        assertTrue(scenario.hasFeedFile());
    }

    @Test
    public void shouldCheckForFeedFileAbsence() {
        Scenario scenario = new Scenario();
        assertFalse(scenario.hasFeedFile());
    }

    @Test
    public void shouldCheckForFeedFileEmpty() {
        Scenario scenario = new Scenario();
        scenario.setFeedFile(Map.of());
        assertFalse(scenario.hasFeedFile());
    }

    @Test
    public void shouldThrowExceptionIfFeedFileIsMissing() {
        Scenario scenario = new Scenario();
        assertThrows(IllegalStateException.class, () -> {
            scenario.feedFile("dev");
        });
    }

    @Test
    public void shouldThrowExceptionIfFeedFileIsNotConfiguredForEnvironment() {
        Scenario scenario = new Scenario();
        Map<String, String> feedFileConfig = Map.of("prod", "prod-data.csv",
                "dev", "dev-data.csv");
        scenario.setFeedFile(feedFileConfig);
        assertThrows(IllegalStateException.class, () -> {
            scenario.feedFile("sit");
        });
    }

    @Test
    public void shouldReturnFeedFileForEnvironment() {
        Scenario scenario = new Scenario();
        String sitFeed = "sit-data.csv";
        Map<String, String> feedFileConfig = Map.of("prod", "prod-data.csv",
                "sit", sitFeed);
        scenario.setFeedFile(feedFileConfig);
        assertEquals(sitFeed, scenario.feedFile("sit"));
    }

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