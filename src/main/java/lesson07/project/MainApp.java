package lesson07.project;

import java.io.IOException;
import java.sql.SQLException;

public class MainApp {

    public static void main(String[] args) throws IOException, SQLException {
        UserInterface userInterface = new UserInterface();
        userInterface.runApplication();
    }
}
