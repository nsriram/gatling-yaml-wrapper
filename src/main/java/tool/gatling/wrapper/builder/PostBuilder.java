package tool.gatling.wrapper.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.CoreDsl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.domain.Body;
import tool.gatling.wrapper.domain.Chain;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.gatling.javaapi.http.HttpDsl.http;
import static java.lang.String.format;

public class PostBuilder extends HttpRequestBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostBuilder.class);

    public PostBuilder(final Chain chain) {
        this.httpRequestActionBuilder = http(chain.getName()).post(chain.getUrl());
        this.addRequestBody(chain);
    }

    private void addRequestBody(final Chain testChain) {
        if (testChain.getBody() == null) {
            return;
        }

        Body body = testChain.getBody();
        try {
            String requestBody = this.requestBodyString(body);
            LOGGER.debug("Request body: {}", requestBody);

            if (body.hasKeys()) {
                List<String> formattedKeys = body.formatELKeys();
                requestBody = format(requestBody, formattedKeys.toArray(new Object[0]));
            }
            this.httpRequestActionBuilder = this.httpRequestActionBuilder.body(CoreDsl.StringBody(requestBody));
        } catch (IOException e) {
            LOGGER.error("Error {} reading template json {} in chain {}", e.getMessage(), body.getTemplateFile(),
                    testChain.getName());
            throw new IllegalArgumentException(format(
                    "Invalid template JSON: %s in chain %s", body.getTemplateFile(), testChain.getName()));
        }
    }

    private String requestBodyString(final Body body) throws IOException {
        String requestTemplateFile = body.getTemplateFile();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(requestTemplateFile));
        return objectMapper.writeValueAsString(jsonNode);
    }
}
