package lesson06;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CheckWeather {
    private final static String SCHEME = "http";
    private final static String BASE_HOST = "dataservice.accuweather.com";
    private final static String FORECAST = "forecasts";
    private final static String API_VERSION = "v1";
    private final static String FORECAST_TYPE = "daily";
    private final static String FORECAST_PERIOD = "5day";
    // LocationCode can be find via http://dataservice.accuweather.com/locations/v1/cities/search request
    private final static String CITY_KEY = "474212_PC";  // Saint Petersburg (28580 - Minsk)
    private final static String API_KEY = "vyYDRAGpczJRODGxcdqqiVGQHNtXDWfp";
    private final static String LANGUAGE = "ru-ru";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .followRedirects(true)
            .retryOnConnectionFailure(true)
            .build();

        HttpUrl url = new HttpUrl.Builder()
            .scheme(SCHEME)
            .host(BASE_HOST)
            .addPathSegment(FORECAST)
            .addPathSegment(API_VERSION)
            .addPathSegment(FORECAST_TYPE)
            .addPathSegment(FORECAST_PERIOD)
            .addPathSegment(CITY_KEY)
            .addQueryParameter("apikey", API_KEY)
            .addQueryParameter("language", LANGUAGE)
            .addQueryParameter("details", "false")
            .addQueryParameter("metric", "true")
            .build();

        System.out.println("URL value: " + url);

        Request request = new Request.Builder()
            .addHeader("accept", "*/*")
            .url(url)
            .build();

        // Get object from server
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            System.out.println("Body: " + body);
            System.out.println("Response code: " + response.code());
            System.out.println("Response headers:" + response.headers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
