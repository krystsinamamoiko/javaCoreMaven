package lesson07.project.provider;

import lesson07.project.entity.WeatherData;
import lesson07.project.enums.Periods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface WeatherProvider {

    public void getWeather(Periods period) throws IOException, SQLException;

    List<WeatherData> getAllFromDb() throws IOException, SQLException;
}
