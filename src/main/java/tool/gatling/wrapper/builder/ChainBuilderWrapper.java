package tool.gatling.wrapper.builder;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.domain.Chain;

import java.util.*;

public class ChainBuilderWrapper {
    private List<ChainBuilder> chainBuilders;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChainBuilderWrapper.class);

    public ChainBuilderWrapper() {
        this.chainBuilders = new ArrayList<>();
    }

    public ChainBuilderWrapper addProperties(final Properties appProperties) {
        this.chainBuilders.add(CoreDsl.exec(session -> addPropertiesToSession(appProperties, session)));
        return this;
    }

    Session addPropertiesToSession(final Properties appProperties, Session session) {
        Set<Object> propertyKeys = appProperties.keySet();
        for (Object propertyKey : propertyKeys) {
            session = session.set(propertyKey.toString(), appProperties.get(propertyKey).toString());
        }
        return session;
    }

    public ChainBuilderWrapper addUUID() {
        this.chainBuilders.add(CoreDsl.exec(session -> session.set("uuid", String.valueOf(UUID.randomUUID()))));
        return this;
    }

    public ChainBuilderWrapper addLogSaveResponseFields(final HttpRequestActionBuilder httpRequestActionBuilder,
                                                        final Chain testChain) {
        chainBuilders.add(CoreDsl.exec(httpRequestActionBuilder)
                .exec(session -> {
                    LOGGER.debug("{}: {}", "statusCode", session.getString("statusCode"));
                    LOGGER.debug("{}: {}", "responseTime", session.getString("responseTime"));
                    for (String saveField : testChain.saveResponseFieldValues()) {
                        LOGGER.debug("{}: {}", saveField, session.getString(saveField));
                        LOGGER.debug("{}: {}", "xApiKey", session.getString("xApiKey"));
                        LOGGER.debug("{}: {}", "uuid", session.getString("uuid"));
                    }
                    return session;
                }));
        return this;
    }

    public List<ChainBuilder> build() {
        return Collections.synchronizedList(chainBuilders);
    }
}
