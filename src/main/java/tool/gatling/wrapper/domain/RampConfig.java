package tool.gatling.wrapper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RampConfig {
    private int users = 0;
    private int duration = -1;

    public Integer getDurationConfig(int maxDuration) {
        return duration < 0 ? maxDuration : duration;
    }
}
