package tool.gatling.wrapper.builder;

import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseCheckBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseCheckBuilder.class);
    private List<CheckBuilder> checkBuilders;

    private boolean isEL(String expectedValue) {
        return expectedValue.startsWith("#{") && expectedValue.endsWith("}");
    }

    public ResponseCheckBuilder() {
        this.checkBuilders = new ArrayList<>();
    }

    public List<CheckBuilder> build() {
        return this.checkBuilders;
    }

    public void addStatus(final Integer status) {
        this.checkBuilders.add(HttpDsl.status().is(status));
    }

    public void addResponseTime(final Integer responseTimeInMillis) {
        this.checkBuilders.add(CoreDsl.responseTimeInMillis().lte(responseTimeInMillis));
    }

    public void addJmesPath(final List<String> jmesPaths) {
        for (String jmesPath : jmesPaths) {
            this.checkBuilders.add(CoreDsl.jmesPath(jmesPath).exists());
        }
    }

    public void addJmesPathChecks(final Map<String, String> jmesPathChecks) {
        for (Map.Entry<String, String> entry : jmesPathChecks.entrySet()) {
            String jmesPath = entry.getKey();
            String expectedValue = entry.getValue();
            LOGGER.debug("Check jmesPath: {}, expectedValue {}", jmesPath, expectedValue);
            this.checkBuilders.add(isEL(expectedValue) ? CoreDsl.jmesPath(jmesPath).isEL(expectedValue) :
                    CoreDsl.jmesPath(jmesPath).is(expectedValue));
        }
    }
}
