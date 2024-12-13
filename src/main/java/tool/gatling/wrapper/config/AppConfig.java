package tool.gatling.wrapper.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.String.format;

@AllArgsConstructor
@Data
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    public static Properties create(final String propertiesPath) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(propertiesPath)) {
            properties.load(input);
        } catch (IOException e) {
            LOGGER.info(format("Properties file not found %s", properties));
            LOGGER.debug("Error reading properties file", e);
            throw new IllegalStateException("Properties file not found", e);
        }
        return properties;
    }
}
