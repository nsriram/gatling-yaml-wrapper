package tool.gatling.wrapper.domain;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class SimulationTest {
    @Test
    public void shouldCheckForBaseUrlPresence() {
        Map<String, String> baseUrlConfig = Map.of("dev", "https://computer-database.gatling.io");
        Simulation simulation = new Simulation();
        simulation.setBaseUrl(baseUrlConfig);
        assertTrue(simulation.hasBaseUrl());
    }

    @Test
    public void shouldCheckForBaseUrlAbsence() {
        Simulation simulation = new Simulation();
        assertFalse(simulation.hasBaseUrl());
    }

    @Test
    public void shouldCheckForBaseUrlEmpty() {
        Simulation simulation = new Simulation();
        simulation.setBaseUrl(Map.of());
        assertFalse(simulation.hasBaseUrl());
    }

    @Test
    public void shouldCheckForPropertiesFilePresence() {
        Map<String, String> propertiesFileConfig = Map.of("dev", "dev.properties");
        Simulation simulation = new Simulation();
        simulation.setPropertiesFile(propertiesFileConfig);
        assertTrue(simulation.hasPropertiesFile());
    }

    @Test
    public void shouldCheckForPropertiesFileAbsence() {
        Simulation simulation = new Simulation();
        assertFalse(simulation.hasPropertiesFile());
    }

    @Test
    public void shouldCheckForPropertiesFileEmpty() {
        Simulation simulation = new Simulation();
        simulation.setPropertiesFile(Map.of());
        assertFalse(simulation.hasPropertiesFile());
    }


    @Test
    public void shouldThrowExceptionWhenEnvironmentIsNotConfigured() {
        Map<String, String> baseUrlConfig = Map.of("dev", "https://computer-database.gatling.io");
        Simulation simulation = new Simulation();
        simulation.setBaseUrl(baseUrlConfig);

        assertThrows(IllegalStateException.class, () -> {
            simulation.envBaseUrl("sit");
        });
    }

    @Test
    public void shouldThrowExceptionWhenEnvironmentIsEmpty() {
        Simulation simulation = new Simulation();
        assertThrows(IllegalStateException.class, () -> {
            simulation.envBaseUrl("dev");
        });
    }

    @Test
    public void shouldReturnBaseUrlForEnvironment() {
        Simulation simulation = new Simulation();
        String devUrl = "https://dev-computer-database.gatling.io";
        Map<String, String> baseUrlConfig = Map.of(
                "dev", devUrl,
                "uat", "https://sit-computer-database.gatling.io",
                "prod", "https://prod-computer-database.gatling.io"
        );
        simulation.setBaseUrl(baseUrlConfig);
        String actualDevUrl = simulation.envBaseUrl("dev");
        assertEquals(devUrl, actualDevUrl);
    }

    @Test
    public void shouldThrowExceptionWhenPropertiesFileIsNotConfigured() {
        Map<String, String> propertiesConfig = Map.of("dev", "dev.properties");
        Simulation simulation = new Simulation();
        simulation.setPropertiesFile(propertiesConfig);

        assertThrows(IllegalStateException.class, () -> {
            simulation.propertiesFilePath("sit");
        });
    }

    @Test
    public void shouldThrowExceptionWhenPropertiesFileIsNull() {
        Simulation simulation = new Simulation();
        assertThrows(IllegalStateException.class, () -> {
            simulation.propertiesFilePath("dev");
        });
    }

    @Test
    public void shouldReturnPropertiesPathForEnvironment() {
        Simulation simulation = new Simulation();
        String sitPropertiesPath = "sit.properties";
        Map<String, String> propertiesConfig = Map.of(
                "dev", "dev.properties",
                "sit", sitPropertiesPath,
                "prod", "prod.properties"
        );
        simulation.setPropertiesFile(propertiesConfig);
        String actualSitUrl = simulation.propertiesFilePath("sit");
        assertEquals(sitPropertiesPath, actualSitUrl);
    }

    @Test
    public void shouldCheckForFeedFilePresence() {
        Map<String, String> feedFile = Map.of("dev", "dev-data.csv");
        Simulation simulation = new Simulation();
        simulation.setFeedFile(feedFile);
        assertTrue(simulation.hasFeedFile());
    }

    @Test
    public void shouldCheckForFeedFileAbsence() {
        Simulation simulation = new Simulation();
        assertFalse(simulation.hasFeedFile());
    }

    @Test
    public void shouldCheckForFeedFileEmpty() {
        Simulation simulation = new Simulation();
        simulation.setFeedFile(Map.of());
        assertFalse(simulation.hasFeedFile());
    }

    @Test
    public void shouldThrowExceptionIfFeedFileIsMissing() {
        Simulation simulation = new Simulation();
        assertThrows(IllegalStateException.class, () -> {
            simulation.feedFile("dev");
        });
    }

    @Test
    public void shouldThrowExceptionIfFeedFileIsNotConfiguredForEnvironment() {
        Simulation simulation = new Simulation();
        Map<String, String> feedFileConfig = Map.of("prod", "prod-data.csv",
                "dev", "dev-data.csv");
        simulation.setFeedFile(feedFileConfig);
        assertThrows(IllegalStateException.class, () -> {
            simulation.feedFile("sit");
        });
    }

    @Test
    public void shouldReturnFeedFileForEnvironment() {
        Simulation simulation = new Simulation();
        String sitFeed = "sit-data.csv";
        Map<String, String> feedFileConfig = Map.of("prod", "prod-data.csv",
                "sit", sitFeed);
        simulation.setFeedFile(feedFileConfig);
        assertEquals(sitFeed, simulation.feedFile("sit"));
    }

}