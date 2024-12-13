package tool.gatling.wrapper.builder;

import io.gatling.javaapi.core.OpenInjectionStep;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.domain.RampConfig;
import tool.gatling.wrapper.domain.Scenario;

import java.util.ArrayList;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;

@NoArgsConstructor
public class InjectionStepsWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectionStepsWrapper.class);

    public OpenInjectionStep[] injectionSteps(final Scenario testScenario, final int maxDuration) {
        List<OpenInjectionStep> injectionSteps = new ArrayList<>();
        injectionSteps.add(atOnceUsers(testScenario.getAtOnceUsers()));
        if (!testScenario.hasRampConfig()) {
            return injectionSteps.toArray(new OpenInjectionStep[0]);
        }

        RampConfig rampConfig = testScenario.getRampConfig();

        LOGGER.debug("RampConfig {}", rampConfig.toString());
        if (rampConfig.getUsers() > 0) {
            injectionSteps.add(rampUsers(rampConfig.getUsers()).during(rampConfig.getDurationConfig(maxDuration)));
        }
        return injectionSteps.toArray(new OpenInjectionStep[0]);
    }
}
