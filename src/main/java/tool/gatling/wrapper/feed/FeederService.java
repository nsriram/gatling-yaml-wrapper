package tool.gatling.wrapper.feed;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.FeederBuilder;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class FeederService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeederService.class);

    public static ChainBuilder feedChain(final String feedFile) {
        LOGGER.debug("Feed file {}", feedFile);
        FeederBuilder.Batchable<String> csv = CoreDsl.csv(feedFile);
        FeederBuilder<String> feederBuilder = csv.circular();
        return CoreDsl.feed(feederBuilder);
    }
}
