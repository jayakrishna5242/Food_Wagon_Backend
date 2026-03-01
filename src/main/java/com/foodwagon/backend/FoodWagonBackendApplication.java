package com.foodwagon.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodWagonBackendApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        setIfPresent(dotenv, "DB_URL");
        setIfPresent(dotenv, "DB_USERNAME");
        setIfPresent(dotenv, "DB_PASSWORD");



        setIfPresent(dotenv, "JWT_SECRET");
        setIfPresent(dotenv, "FRONTEND_URL");

        setIfPresent(dotenv,"BREVO_SMTP_USER");
        setIfPresent(dotenv,"BREVO_SMTP_PASS");
        setIfPresent(dotenv,"USER_EMAIL");
        setIfPresent(dotenv,"SMTP_HOST");setIfPresent(dotenv,"SMTP_PORT");

        SpringApplication.run(FoodWagonBackendApplication.class, args);
    }
    private static void setIfPresent(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value != null) {
            System.setProperty(key, value);
        }
    }
}