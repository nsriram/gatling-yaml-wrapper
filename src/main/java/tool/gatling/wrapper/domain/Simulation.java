package tool.gatling.wrapper.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Simulation {
    private Map<String, String> baseUrl;
    private Map<String, String> propertiesFile;
    private String acceptHeader;
    private String contentTypeHeader;
    private int maxDuration;
    private List<Scenario> scenarios;

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    boolean hasBaseUrl() {
        return baseUrl != null && !baseUrl.isEmpty();
    }

    public boolean hasPropertiesFile() {
        return propertiesFile != null && !propertiesFile.isEmpty();
    }

    public String envBaseUrl(final String env) {
        if (!hasBaseUrl() || !baseUrl.containsKey(env)) {
            LOGGER.debug("No baseUrl configured for env {}", env);
            throw new IllegalStateException("No baseUrl found");
        }

        String simulationBaseUrl = baseUrl.get(env);
        LOGGER.debug("Simulation base URL {}", simulationBaseUrl);
        return simulationBaseUrl;
    }

    public String propertiesFilePath(final String env) {
        if (!hasPropertiesFile() || !propertiesFile.containsKey(env)) {
            LOGGER.debug("No properties configured for env {}", env);
            throw new IllegalStateException("No properties file config found");
        }

        String simulationPropertiesFilePath = propertiesFile.get(env);
        LOGGER.debug("Simulation file path {}", simulationPropertiesFilePath);
        return simulationPropertiesFilePath;
    }
}
