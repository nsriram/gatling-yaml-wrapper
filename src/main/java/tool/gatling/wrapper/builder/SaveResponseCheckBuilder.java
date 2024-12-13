package tool.gatling.wrapper.builder;

import io.gatling.javaapi.core.CheckBuilder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.responseTimeInMillis;
import static io.gatling.javaapi.http.HttpDsl.status;

@Data
public class SaveResponseCheckBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveResponseCheckBuilder.class);
    private List<CheckBuilder> checkBuilders;

    public SaveResponseCheckBuilder() {
        this.checkBuilders = new ArrayList<>();
        this.addDefaultFields();
    }

    public List<CheckBuilder> build() {
        return checkBuilders;
    }

    void addDefaultFields() {
        this.checkBuilders.add(status().saveAs("statusCode"));
        this.checkBuilders.add(responseTimeInMillis().saveAs("responseTime"));
    }

    public SaveResponseCheckBuilder addSaveResponseFields(final Map<String, String> saveResponseFields) {
        for (Map.Entry<String, String> entry : saveResponseFields.entrySet()) {
            String jmesPath = entry.getKey();
            String savedValue = entry.getValue();
            LOGGER.debug("Check jmesPath: {}, savedValue {}", jmesPath, savedValue);
            checkBuilders.add(jmesPath(jmesPath).saveAs(savedValue));
        }
        return this;
    }
}
