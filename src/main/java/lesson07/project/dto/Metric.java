
package lesson07.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Metric {
    @JsonProperty(value = "Unit")
    private String mUnit;
    @JsonProperty(value = "UnitType")
    private Long mUnitType;
    @JsonProperty(value = "Value")
    private Double mValue;

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    public Long getUnitType() {
        return mUnitType;
    }

    public void setUnitType(Long unitType) {
        mUnitType = unitType;
    }

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        mValue = value;
    }

}
