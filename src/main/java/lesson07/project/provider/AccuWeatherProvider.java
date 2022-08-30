package lesson07.project.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lesson07.project.config.ApplicationGlobalState;
import lesson07.project.dto.WeatherResponse;
import lesson07.project.entity.WeatherData;
import lesson07.project.enums.Languages;
import lesson07.project.enums.Periods;
import lesson07.project.repository.DatabaseRepository;
import lesson07.project.repository.DatabaseRepositorySQLiteImpl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccuWeatherProvider implements WeatherProvider {

    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECAST_ENDPOINT = "forecasts";
    private final static String FORECAST_TYPE = "daily";
    private final static String FORECAST_PERIOD = "5day";
    private static final String CURRENT_CONDITIONS_ENDPOINT = "currentconditions";
    private static final String API_VERSION = "v1";
    private static final String API_KEY = ApplicationGlobalState.getInstance().getApiKey();
    private static final Languages LANGUAGE = ApplicationGlobalState.getInstance().getLanguage();

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DatabaseRepository repository = new DatabaseRepositorySQLiteImpl();

    @Override
    public void getWeather(Periods periods) throws IOException, SQLException {
        String cityKey = detectCityKey();

        if (periods.equals(Periods.NOW)) {
            HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                .addPathSegment(API_VERSION)
                .addPathSegment(cityKey)
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("language", LANGUAGE.getCode())
                .addQueryParameter("metric", "true")
                .build();

            Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url)
                .build();

            String responseBody = client.newCall(request).execute().body().string();

            List<WeatherResponse> weatherList = objectMapper.readValue(responseBody, new TypeReference<List<WeatherResponse>>() {});

            WeatherResponse weatherItem = weatherList.get(0);
            System.out.println("At the current moment in city " + ApplicationGlobalState.getInstance().getSelectedCity() +
                " temperature "  + weatherItem.getTemperature().getMetric().getValue() + "°С, and " + weatherItem.getWeatherText() + ".");
        }
        if (periods.equals(Periods.FIVE_DAYS)) {
            HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment(FORECAST_ENDPOINT)
                .addPathSegment(API_VERSION)
                .addPathSegment(FORECAST_TYPE)
                .addPathSegment(FORECAST_PERIOD)
                .addPathSegment(cityKey)
                .addQueryParameter("apikey", API_KEY)
                .addQueryParameter("language", LANGUAGE.getCode())
                .addQueryParameter("details", "false")
                .addQueryParameter("metric", "true")
                .build();

            Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url)
                .build();

            String responseBody = client.newCall(request).execute().body().string();

            if (objectMapper.readTree(responseBody).size() > 0) {
                String forecastInfo = objectMapper.readTree(responseBody).at("/DailyForecasts").toString();
                List<WeatherResponse> weatherList = objectMapper.readValue(forecastInfo, new TypeReference<List<WeatherResponse>>() {});

                for (WeatherResponse weather: weatherList) {
                    System.out.println("In city " + ApplicationGlobalState.getInstance().getSelectedCity() + " on date " + weather.getDate().substring(0,10) +
                        " the following weather conditions are being expected: Minimum temperature "  + weather.getTemperature().getMinimum().getValue() + "°С. Maximum temperature " +
                        weather.getTemperature().getMaximum().getValue() + "°С. Day - " + weather.getDay().getIconPhrase() + ". Night - " + weather.getNight().getIconPhrase() + ".");

                    WeatherData weatherData = new WeatherData(ApplicationGlobalState.getInstance().getSelectedCity(),
                        weather.getDate().substring(0,10), weather.getDay().getIconPhrase(), weather.getNight().getIconPhrase(),
                        weather.getTemperature().getMinimum().getValue(), weather.getTemperature().getMaximum().getValue()
                    );

                    repository.saveWeatherData(weatherData);
                }
            } else throw new IOException("Weather forecasts are not available");
        }
        if (periods.equals(Periods.BASE)) {
            getAllFromDb();
        }
    }

    @Override
    public List<WeatherData> getAllFromDb() throws SQLException, IOException {
        List<WeatherData> weatherDataList = repository.getAllSavedData();
        for (WeatherData weatherData : weatherDataList) {
            System.out.println(weatherData);
        }
        return weatherDataList;
    }

    public String detectCityKey() throws IOException {
        String selectedCity = ApplicationGlobalState.getInstance().getSelectedCity();

        HttpUrl detectLocationURL = new HttpUrl.Builder()
            .scheme("http")
            .host(BASE_HOST)
            .addPathSegment("locations")
            .addPathSegment(API_VERSION)
            .addPathSegment("cities")
            .addPathSegment("autocomplete")
            .addQueryParameter("apikey", API_KEY)
            .addQueryParameter("q", selectedCity)
            .addQueryParameter("language", LANGUAGE.getCode())
            .build();

        Request request = new Request.Builder()
            .addHeader("accept", "application/json")
            .url(detectLocationURL)
            .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("It is impossible to get city information. " +
                "Server response code system. = " + response.code() + " response body = " + response.body().string());
        }
        String jsonResponse = response.body().string();
        System.out.println("Searching for the city " + selectedCity);

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            String cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
            System.out.println("City " + cityName + " has ben found in " + countryName + " country");
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }
}
