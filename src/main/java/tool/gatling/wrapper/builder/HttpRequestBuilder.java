package tool.gatling.wrapper.builder;

import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import tool.gatling.wrapper.domain.Chain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class HttpRequestBuilder {
    protected HttpRequestActionBuilder httpRequestActionBuilder;

    public HttpRequestActionBuilder build() {
        return this.httpRequestActionBuilder;
    }

    public HttpRequestBuilder addHeaders(final Chain testChain) {
        if (testChain.hasHeaders()) {
            Map<String, String> headers = testChain.getHeaders();
            Set<String> headerNames = headers.keySet();
            for (String headerName : headerNames) {
                this.httpRequestActionBuilder = this.httpRequestActionBuilder
                        .header(headerName, headers.get(headerName));
            }
        }
        return this;
    }

    public HttpRequestBuilder addResponseFields(final Chain chain) {
        List<CheckBuilder> saveResponseBuilders = chain.saveResponseFieldsWithDefault();
        for (CheckBuilder checkBuilder : saveResponseBuilders) {
            this.httpRequestActionBuilder = this.httpRequestActionBuilder.check(checkBuilder);
        }
        return this;
    }

    public HttpRequestBuilder addChecks(final Chain chain) {
        List<CheckBuilder> checkBuilders = chain.checkBuilders();
        for (CheckBuilder checkBuilder : checkBuilders) {
            httpRequestActionBuilder = httpRequestActionBuilder.check(checkBuilder);
        }
        return this;
    }
}
