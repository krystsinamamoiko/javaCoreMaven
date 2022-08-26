package lesson07.project;

import lesson07.project.config.ApplicationGlobalState;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface {

    private final Controller controller = new Controller();

    public void runApplication() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("Make your choice and enter it: 1 - Get the current weather, " +
                "2 - Get 5 day weather forecast, " +
                "3 - Exit the application");
            String result = scanner.nextLine();

            try {
                validateUserInput(result);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            checkIsExit(result);

            if (result.equals("1") || result.equals("2")) {
                System.out.println("Enter the city name (use '" + ApplicationGlobalState.getInstance().getLanguage().getTitle() + "' language):");
                String city = scanner.nextLine();
                setGlobalCity(city);
            }

            try {
                notifyController(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void checkIsExit(String result) throws IOException {
        if (result.equals("3")) {
            controller.exitApp();
        }
    }

    private void setGlobalCity(String city) {
        ApplicationGlobalState.getInstance().setSelectedCity(city);
    }


    private void validateUserInput(String userInput) throws IOException {
        if (userInput == null || userInput.length() != 1) {
            throw new IOException("Incorrect user input: expected one digit as answer, but actually get " + userInput);
        }
        if (userInput == null || userInput.length() != 1) {
            throw new IOException("Incorrect user input: expected one digit as answer, but actually get " + userInput);
        }
        int answer;
        try {
            answer = Integer.parseInt(userInput);
            if (answer >= 4){
                throw new IOException("Incorrect user input: character must be less then 5!");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Incorrect user input: character is not numeric!");
        }
    }

    private void notifyController(String input) throws IOException {
        controller.onUserInput(input);
    }
}
