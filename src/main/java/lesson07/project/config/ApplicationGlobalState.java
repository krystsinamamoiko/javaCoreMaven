package lesson07.project.config;

import lesson07.project.enums.Languages;

public final class ApplicationGlobalState {

    private static ApplicationGlobalState INSTANCE;
    private String selectedCity = null;
    private final String API_KEY = "vyYDRAGpczJRODGxcdqqiVGQHNtXDWfp";
    private final Languages LANGUAGE = Languages.RUSSIAN;

    private ApplicationGlobalState() {
    }

    // It is not thread safety code (it is used just to simplify implementation)
    public static ApplicationGlobalState getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ApplicationGlobalState();
        }
        return INSTANCE;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public String getApiKey() {
        return this.API_KEY;
    }

    public Languages getLanguage() {
        return this.LANGUAGE;
    }
}
