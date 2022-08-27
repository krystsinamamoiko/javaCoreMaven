
package lesson07.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lesson07.project.dto.Imperial;
import lesson07.project.dto.Maximum;
import lesson07.project.dto.Metric;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Temperature {

    @JsonProperty(value = "Imperial")
    private Imperial imperial;

    @JsonProperty(value = "Metric")
    private Metric metric;

    @JsonProperty(value = "Maximum")
    private Maximum maximum;

    @JsonProperty(value = "Minimum")
    private Minimum minimum;

    @Override
    public String toString() {
        return "Temperature{" +
            "Metric=" + metric +
            ", Minimum=" + minimum +
            ", Maximum=" + maximum +
            ", Imperial=" + imperial +
            '}';
    }

}
