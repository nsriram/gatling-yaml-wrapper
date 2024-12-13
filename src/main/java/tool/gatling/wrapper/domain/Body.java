package tool.gatling.wrapper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Body {
    private String templateFile;
    private List<String> keys;

    public boolean hasKeys() {
        return keys != null && !keys.isEmpty();
    }

    public List<String> formatELKeys() {
        if (!hasKeys()) {
            return new ArrayList<>();
        }
        return this.getKeys().stream().map(key -> "#{" + key + "}").toList();
    }
}
