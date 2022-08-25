
package lesson07.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Minimum {

    @JsonProperty(value = "Unit")
    private String unit;

    @JsonProperty(value = "UnitType")
    private Long unitType;

    @JsonProperty(value = "Value")
    private Double value;

    @Override
    public String toString() {
        return "Minimum{" +
            "Unit='" + unit + '\'' +
            ", UnitType=" + unitType +
            ", Value=" + value +
            '}';
    }

}
