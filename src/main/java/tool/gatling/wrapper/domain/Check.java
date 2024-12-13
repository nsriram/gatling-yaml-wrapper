package tool.gatling.wrapper.domain;

import io.gatling.javaapi.core.CheckBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.builder.ResponseCheckBuilder;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Check {
    private Integer status;
    private Integer responseTimeInMillis;
    private List<String> jmesPaths;
    private Map<String, String> jmesPathChecks;

    private static final Logger LOGGER = LoggerFactory.getLogger(Check.class);

    public List<CheckBuilder> checkBuilders() {
        ResponseCheckBuilder responseCheckBuilder = new ResponseCheckBuilder();
        if (status != null) {
            responseCheckBuilder.addStatus(status);
        }

        if (responseTimeInMillis != null) {
            responseCheckBuilder.addResponseTime(responseTimeInMillis);
        }

        if (hasJmesPath()) {
            responseCheckBuilder.addJmesPath(jmesPaths);
        }

        if (hasJmesPathChecks()) {
            responseCheckBuilder.addJmesPathChecks(jmesPathChecks);
        }

        return responseCheckBuilder.build();
    }

    boolean hasJmesPathChecks() {
        return jmesPathChecks != null && !jmesPathChecks.isEmpty();
    }

    boolean hasJmesPath() {
        return jmesPaths != null && !jmesPaths.isEmpty();
    }
}
