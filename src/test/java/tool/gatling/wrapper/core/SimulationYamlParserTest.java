package tool.gatling.wrapper.core;

import org.junit.Test;
import tool.gatling.wrapper.domain.Chain;
import tool.gatling.wrapper.domain.RampConfig;
import tool.gatling.wrapper.domain.Scenario;
import tool.gatling.wrapper.domain.Simulation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationYamlParserTest {
    @Test
    public void shouldParseSimpleSimulation() {
        Simulation simulation = SimulationYamlParser.parse("simple/simulation.yml");

        assertEquals(5, simulation.getMaxDuration());
        assertEquals("application/html", simulation.getAcceptHeader());
        assertEquals("application/html", simulation.getContentTypeHeader());
        assertEquals(1, simulation.getScenarios().size());

        Scenario firstScenario = simulation.getScenarios().get(0);
        assertEquals("computer database", firstScenario.getName());
        assertEquals(1, firstScenario.getAtOnceUsers());

        RampConfig rampConfig = firstScenario.getRampConfig();
        assertEquals(1, rampConfig.getUsers());
        assertEquals(2, rampConfig.getDuration());

        List<Chain> chains = firstScenario.getChains();
        assertEquals(3, chains.size());

        assertChain(chains.get(0), "all computers", "/computers/", "GET", Map.of());
        assertChain(chains.get(1), "macbook", "/computers/", "GET", Map.of("f", "macbook"));
        assertChain(chains.get(2), "PC second page", "/computers/", "GET", Map.of("f", "pc", "p", "1"));

        Chain secondChain = chains.get(1);
        assertEquals("macbook", secondChain.getName());
        assertEquals("/computers/", secondChain.getUrl());
        assertEquals("GET", secondChain.getMethod());

        Chain thirdChain = chains.get(2);
        assertEquals("PC second page", thirdChain.getName());
        assertEquals("/computers/", thirdChain.getUrl());
        assertEquals("GET", thirdChain.getMethod());
    }

    private void assertChain(Chain chain, String name, String url, String method, Map<String, String> queryParams) {
        assertEquals(name, chain.getName());
        assertEquals(url, chain.getUrl());
        assertEquals(method, chain.getMethod());

        if (queryParams != null && !queryParams.isEmpty()) {
            assertEquals(queryParams, chain.getQueryParams());
        }
    }
}