package tool.gatling.wrapper.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.domain.Chain;

import java.util.Arrays;
import java.util.List;

public class HttpRequestActionBuilderFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestActionBuilderFactory.class);

    private final List<String> supportedHttpMethods = Arrays.asList("GET", "POST");

    public HttpRequestBuilder getHttpRequestBuilder(final Chain chain) {
        String requestMethod = chain.getMethod();
        if (!supportedHttpMethods.contains(requestMethod)) {
            LOGGER.error("Unsupported HTTP method in chain {}, {}", chain.getName(), chain.getMethod());
            throw new IllegalArgumentException("Unsupported HTTP method: " + requestMethod);
        }

        HttpRequestBuilder requestBuilder = null;
        if (requestMethod.equals("GET")) {
            requestBuilder = new GetBuilder(chain);
        } else if (requestMethod.equals("POST")) {
            requestBuilder = new PostBuilder(chain);
        }
        return requestBuilder;
    }
}
