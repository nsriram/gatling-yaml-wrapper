package tool.gatling.wrapper.domain;

import io.gatling.javaapi.core.CheckBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.builder.SaveResponseCheckBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chain {
    private String name;
    private String url;
    private String method;
    private List<String> signatureFields;
    private Map<String, String> headers;
    private Body body;
    private Map<String, String> queryParams;
    private Check check;
    private Map<String, String> saveResponseFields;

    private static final Logger LOGGER = LoggerFactory.getLogger(Check.class);

    public boolean hasHeaders() {
        return headers != null && !headers.isEmpty();
    }

    public boolean hasSignatureFields() {
        return signatureFields != null && !signatureFields.isEmpty();
    }

    public List<CheckBuilder> checkBuilders() {
        if (check == null) {
            return List.of();
        }
        return check.checkBuilders();
    }

    boolean hasSaveResponseFields() {
        return saveResponseFields != null && !saveResponseFields.isEmpty();
    }

    public List<String> saveResponseFieldValues() {
        if (!hasSaveResponseFields()) {
            return List.of();
        }
        return new ArrayList<>(saveResponseFields.values());
    }

    public List<CheckBuilder> saveResponseFieldsWithDefault() {
        if (!hasSaveResponseFields()) {
            return new SaveResponseCheckBuilder().build();
        }
        return new SaveResponseCheckBuilder()
                .addSaveResponseFields(saveResponseFields)
                .build();
    }


    public boolean hasQueryParams() {
        return queryParams != null && !queryParams.isEmpty();
    }
}
