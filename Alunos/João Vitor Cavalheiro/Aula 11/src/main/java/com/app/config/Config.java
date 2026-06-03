package com.app.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv dotenv =
            Dotenv.configure()
                  .load();

    public static String getApiKey() {
        return dotenv.get("API_KEY");
    }
}