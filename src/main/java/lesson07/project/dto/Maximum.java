package lesson07.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Maximum {

    @JsonProperty(value = "Unit")
    private String unit;

    @JsonProperty(value = "UnitType")
    private Long unitType;

    @JsonProperty(value = "Value")
    private Double value;

    @Override
    public String toString() {
        return "Maximum{" +
            "Unit='" + unit + '\'' +
            ", UnitType=" + unitType +
            ", Value=" + value +
            '}';
    }

}
