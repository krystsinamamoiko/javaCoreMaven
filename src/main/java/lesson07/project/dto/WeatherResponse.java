
package lesson07.project.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherResponse {

    @JsonProperty(value = "Date")
    @JsonAlias(value = "LocalObservationDateTime")
    private String date;

    @JsonProperty(value = "Temperature")
    private Temperature temperature;

    @JsonProperty(value = "WeatherText")
    private String weatherText;

    @JsonProperty(value = "Day")
    private Day day;

    @JsonProperty(value = "Night")
    private Night night;

    @Override
    public String toString() {
        return "WeatherResponse{" +
            ", Date='" + date + '\'' +
            ", WeatherText='" + weatherText + '\'' +
            ", Temperature=" + temperature +
            ", Day=" + day +
            ", Night=" + night +
            '}';
    }

}
