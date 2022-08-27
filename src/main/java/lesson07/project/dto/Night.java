
package lesson07.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Night {

    @JsonProperty(value = "HasPrecipitation")
    private Boolean hasPrecipitation;

    @JsonProperty(value = "Icon")
    private Long icon;

    @JsonProperty(value = "IconPhrase")
    private String iconPhrase;

    @Override
    public String toString() {
        return "Night{" +
            "Icon=" + icon +
            ", IconPhrase='" + iconPhrase + '\'' +
            ", HasPrecipitation=" + hasPrecipitation + '\'' +
            '}';
    }
}
