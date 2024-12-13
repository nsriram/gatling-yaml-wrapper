package tool.gatling.wrapper.core;

import io.gatling.app.RunResult;
import io.gatling.app.RunResultProcessor;
import io.gatling.app.Runner;
import io.gatling.app.cli.GatlingArgsParser;
import io.gatling.app.cli.StatusCode;
import io.gatling.commons.util.DefaultClock;
import io.gatling.core.actor.ActorSystem;
import io.gatling.core.cli.GatlingArgs;
import io.gatling.core.config.GatlingConfiguration;
import io.gatling.netty.util.Transports;
import io.netty.channel.EventLoopGroup;
import org.junit.Test;
import scala.util.Either;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FrameworkSimulationTest {
    @Test
    public void shouldRunGatlingSimulationForGetAndPostAPIs() {
        System.setProperty("env", "dev");
        System.getProperty("simulation");
        System.setProperty("simulation", "reqres/simulation.yml");
        String[] args = {"--simulation", "tool.gatling.wrapper.core.FrameworkSimulation",
                "--results-folder", "target/gatling"};

        GatlingArgs gatlingArgs = gatlingArgs(args);
        GatlingConfiguration configuration = GatlingConfiguration.load();
        EventLoopGroup eventLoopGroup = Transports.newEventLoopGroup(
                configuration.netty().useNativeTransport(),
                configuration.netty().useIoUring(),
                0,
                "gatling");
        ActorSystem system = new ActorSystem();
        Runner runner = new Runner(system, eventLoopGroup, new DefaultClock(), gatlingArgs, configuration);

        RunResult result = runner.run();

        StatusCode statusCode = processPerfTestRun(gatlingArgs, configuration, result);
        assertEquals(0, statusCode.code());
        assertPerfResultsSimulationLogExists(result);
        system.close();
    }

    private StatusCode processPerfTestRun(GatlingArgs gatlingArgs, GatlingConfiguration configuration, RunResult result) {
        RunResultProcessor runResultProcessor = new RunResultProcessor(gatlingArgs, configuration);
        return runResultProcessor.processRunResult(result);
    }

    private GatlingArgs gatlingArgs(String[] args) {
        Either<GatlingArgs, StatusCode> gatlingArgsStatusCodeEither = new GatlingArgsParser(args).parseArguments();
        return gatlingArgsStatusCodeEither.left().get();
    }

    private void assertPerfResultsSimulationLogExists(RunResult result) {
        String perfTestId = result.runId();
        File perfTestResultFile = new File("target/gatling/", perfTestId + "/simulation.log");
        assertTrue(perfTestResultFile.exists());
    }
}