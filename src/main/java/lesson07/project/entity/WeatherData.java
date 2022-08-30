package lesson07.project.entity;

public class WeatherData {
    private String city;
    private String date;
    private String dayText;
    private String nightText;
    private Double minTemperature;
    private Double maxTemperature;

    public WeatherData() {
    }

    public WeatherData(String city, String date, String dayText, String nightText, Double minTemperature, Double maxTemperature) {
        this.city = city;
        this.date = date;
        this.dayText = dayText;
        this.nightText = nightText;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    public String getDayText() {
        return dayText;
    }

    public String getNightText() {
        return nightText;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
            "city='" + city + '\'' +
            ", date='" + date + '\'' +
            ", dayText='" + dayText + '\'' +
            ", nightText='" + nightText + '\'' +
            ", minTemperature=" + minTemperature +
            ", maxTemperature=" + maxTemperature +
            '}';
    }
}
