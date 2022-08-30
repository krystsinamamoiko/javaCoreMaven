package lesson07.project.repository;

import lesson07.project.config.ApplicationGlobalState;
import lesson07.project.entity.WeatherData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRepositorySQLiteImpl implements DatabaseRepository {

    private static Connection connection;
    private static Statement statement;
    String filename;
    String selectAllSQLQuery = "SELECT * FROM weather";
    String createTableSQLQuery = "CREATE TABLE IF NOT EXISTS weather (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "city TEXT NOT NULL, date_time TEXT NOT NULL, weather_day_text TEXT NOT NULL, " +
        "weather_night_text TEXT NOT NULL, min_temperature REAL NOT NULL, max_temperature REAL NOT NULL);";
    String insertWeatherSQLQuery = "INSERT INTO weather (city, date_time, weather_day_text, weather_night_text, " +
        "min_temperature, max_temperature) VALUES (?,?,?,?,?,?)";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DatabaseRepositorySQLiteImpl() {
        this.filename = ApplicationGlobalState.getInstance().getDbFileName();
    }

    public Connection getConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        return connection;
    }

    public Statement getStatement() throws SQLException {
        if (statement == null)
            statement = getConnection().createStatement();
        return statement;
    }

    public PreparedStatement getPreparedStatement() throws SQLException{
        return getConnection().prepareStatement(insertWeatherSQLQuery);
    }

    public void createTableIfNotExists() {
        try (Statement statement = getStatement()) {
            statement.executeUpdate(createTableSQLQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean saveWeatherData(WeatherData weatherData) throws SQLException {
        createTableIfNotExists();
        try (PreparedStatement saveWeather = getPreparedStatement()) {
            saveWeather.setString(1, weatherData.getCity());
            saveWeather.setString(2, weatherData.getDate());
            saveWeather.setString(3, weatherData.getDayText());
            saveWeather.setString(4, weatherData.getNightText());
            saveWeather.setDouble(5, weatherData.getMinTemperature());
            saveWeather.setDouble(6, weatherData.getMaxTemperature());

            return saveWeather.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new SQLException("Failure on saving weather object");
    }

    @Override
    public List<WeatherData> getAllSavedData() throws SQLException {
        ResultSet resultSet = getConnection().createStatement().executeQuery(selectAllSQLQuery);
        List<WeatherData> weatherDataList = new ArrayList<>();
        while (resultSet.next()) {
            weatherDataList.add(new WeatherData(resultSet.getString(2), resultSet.getString(3),  resultSet.getString(4),
                resultSet.getString(5), resultSet.getDouble(6), resultSet.getDouble(7)));
        }
        return weatherDataList;
    }

    @Override
    public void closeConnection() {
        try {
            getPreparedStatement().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            getStatement().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            getConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
