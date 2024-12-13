package tool.gatling.wrapper.core;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import tool.gatling.wrapper.domain.Simulation;

import java.io.InputStream;

public class SimulationYamlParser {
    public static Simulation parse(final String file) {
        Yaml yaml = new Yaml(new Constructor(Simulation.class, new LoaderOptions()));
        InputStream inputStream = SimulationYamlParser.class
                .getClassLoader()
                .getResourceAsStream(file);
        return yaml.load(inputStream);
    }
}
