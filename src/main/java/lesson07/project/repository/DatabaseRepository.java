package lesson07.project.repository;

import lesson07.project.entity.WeatherData;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepository {

    boolean saveWeatherData(WeatherData weatherData) throws SQLException;

    List<WeatherData> getAllSavedData() throws SQLException;

    void closeConnection();
}
