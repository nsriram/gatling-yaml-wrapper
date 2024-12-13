package tool.gatling.wrapper.builder;

import lombok.NoArgsConstructor;
import tool.gatling.wrapper.domain.Chain;

import java.util.Set;

import static io.gatling.javaapi.http.HttpDsl.http;

@NoArgsConstructor
public class GetBuilder extends HttpRequestBuilder {
    public GetBuilder(final Chain chain) {
        this.httpRequestActionBuilder = http(chain.getName()).get(chain.getUrl());
        this.addQueryParams(chain);
    }

    private void addQueryParams(final Chain chain) {
        if (chain.hasQueryParams()) {
            Set<String> keys = chain.getQueryParams().keySet();
            for (String key : keys) {
                this.httpRequestActionBuilder = this.httpRequestActionBuilder.queryParam(
                        key, chain.getQueryParams().get(key));
            }
        }
    }
}
