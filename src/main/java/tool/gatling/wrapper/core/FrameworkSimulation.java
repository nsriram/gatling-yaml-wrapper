package tool.gatling.wrapper.core;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.gatling.wrapper.builder.ChainBuilderWrapper;
import tool.gatling.wrapper.builder.HttpRequestActionBuilderFactory;
import tool.gatling.wrapper.builder.HttpRequestBuilder;
import tool.gatling.wrapper.builder.InjectionStepsWrapper;
import tool.gatling.wrapper.config.AppConfig;
import tool.gatling.wrapper.domain.Simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.group;
import static tool.gatling.wrapper.feed.FeederService.feedChain;

public class FrameworkSimulation extends io.gatling.javaapi.core.Simulation {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkSimulation.class);
    private static String env;
    private static Simulation simulationRecipe;
    private static List<PopulationBuilder> populationBuilders;

    static {
        populationBuilders = Collections.synchronizedList(new ArrayList<>());
        env = System.getProperty("env", "dev");
        String simulationRecipeFile = System.getProperty("simulation", "simple/simulation.yml");
        simulationRecipe = SimulationYamlParser.parse(simulationRecipeFile);

        String propertiesPath = "";
        Properties appProperties = new Properties();
        if (simulationRecipe.hasPropertiesFile()) {
            LOGGER.debug("loading {}.properties", env);
            propertiesPath = simulationRecipe.propertiesFilePath(env);
            LOGGER.debug("propertiesPath, {}", propertiesPath);
            appProperties = AppConfig.create(propertiesPath);
        }

        for (var testScenario : simulationRecipe.getScenarios()) {
            ChainBuilderWrapper chainBuilderWrapper = new ChainBuilderWrapper()
                    .addProperties(appProperties)
                    .addUUID();

            for (var testChain : testScenario.getChains()) {
                HttpRequestBuilder httpRequestBuilder = new HttpRequestActionBuilderFactory()
                        .getHttpRequestBuilder(testChain);
                httpRequestBuilder.addHeaders(testChain);
                httpRequestBuilder.addChecks(testChain).addResponseFields(testChain);
                HttpRequestActionBuilder httpRequestActionBuilder = httpRequestBuilder.build();

                chainBuilderWrapper.addLogSaveResponseFields(httpRequestActionBuilder, testChain);
            }

            List<ChainBuilder> chainBuilders = chainBuilderWrapper.build();

            ScenarioBuilder scenarioBuilder = CoreDsl.scenario(testScenario.getName())
                    .during(testScenario.getDuration())
                    .on(exec(chainBuilders));

            if (simulationRecipe.hasFeedFile()) {
                scenarioBuilder = CoreDsl.scenario(testScenario.getName())
                        .during(testScenario.getDuration())
                        .on(group(testScenario.getName() + "  group")
                                .on(feedChain(simulationRecipe.feedFile(env)).exec(chainBuilders))
                        );
            }

            InjectionStepsWrapper injectionStepsWrapper = new InjectionStepsWrapper();
            populationBuilders.add(scenarioBuilder
                    .injectOpen(injectionStepsWrapper.injectionSteps(testScenario, simulationRecipe.getMaxDuration()))
            );
        }
    }

    {
        HttpProtocolBuilder httpProtocolBuilder = HttpDsl.http
                .baseUrl(simulationRecipe.envBaseUrl(env))
                .acceptHeader(simulationRecipe.getAcceptHeader())
                .contentTypeHeader(simulationRecipe.getContentTypeHeader());

        setUp(populationBuilders)
                .maxDuration(simulationRecipe.getMaxDuration())
                .protocols(httpProtocolBuilder);
    }
}
