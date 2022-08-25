package lesson07.project.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lesson07.project.config.ApplicationGlobalState;
import lesson07.project.dto.WeatherResponse;
import lesson07.project.enums.Languages;
import lesson07.project.enums.Periods;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    public void getWeather(Periods periods) throws IOException {
        String cityKey = detectCityKey();
        HttpUrl url;
        Request request;
        String responseBody = "";
        List<WeatherResponse> weatherList = new ArrayList<>();
        switch(periods) {
            case NOW:
                url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", LANGUAGE.getCode())
                    .addQueryParameter("metric", "true")
                    .build();

                request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

                responseBody = client.newCall(request).execute().body().string();

                weatherList = objectMapper.readValue(responseBody, new TypeReference<List<WeatherResponse>>() {});

                WeatherResponse weatherItem = weatherList.get(0);
                System.out.println("На данный момент в городе " + ApplicationGlobalState.getInstance().getSelectedCity() +
                    " температура "  + weatherItem.getTemperature().getMetric().getValue() + "°С, и " + weatherItem.getWeatherText() + ".");
                break;
            case FIVE_DAYS:
                url = new HttpUrl.Builder()
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

                request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

                responseBody = client.newCall(request).execute().body().string();

                int firstIndexBody = responseBody.indexOf("[{\"Date\"");
                int lastIndexBody = responseBody.lastIndexOf("}");
                responseBody = responseBody.substring(firstIndexBody, lastIndexBody);

                weatherList = objectMapper.readValue(responseBody, new TypeReference<List<WeatherResponse>>() {});

                for (WeatherResponse weather: weatherList) {
                    System.out.println("В городе " + ApplicationGlobalState.getInstance().getSelectedCity() + " на дату " + weather.getDate().substring(0,10) +
                        " ожидается следующая погода: Минимальная температура "  + weather.getTemperature().getMinimum().getValue() + "°С. Максимальная температура " +
                        weather.getTemperature().getMaximum().getValue() + "°С. Днём - " + weather.getDay().getIconPhrase() + ". Ночью - " + weather.getNight().getIconPhrase() + ".");
                }
                break;
            default: {
                throw new RuntimeException("A handler for '" + periods.name() + "' period is not still implemented");
            }
        }
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
            throw new IOException("Невозможно прочесть информацию о городе. " +
                "Код ответа сервера = " + response.code() + " тело ответа = " + response.body().string());
        }
        String jsonResponse = response.body().string();
        System.out.println("Произвожу поиск города " + selectedCity);

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            String cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
            System.out.println("Найден город " + cityName + " в стране " + countryName);
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }
}
