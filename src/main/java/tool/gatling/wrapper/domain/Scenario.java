package tool.gatling.wrapper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scenario {
    private String name;
    private int atOnceUsers = 1;
    private int duration = 1;
    private RampConfig rampConfig;
    private List<Chain> chains;

    private static final Logger LOGGER = LoggerFactory.getLogger(Scenario.class);

    public boolean hasRampConfig() {
        return rampConfig != null;
    }
}
