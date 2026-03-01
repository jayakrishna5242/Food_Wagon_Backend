package com.foodwagon.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync

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

        setIfPresent(dotenv,"BREVO_API_KEY");
        setIfPresent(dotenv,"BREVO_SENDER_EMAIL");

        SpringApplication.run(FoodWagonBackendApplication.class, args);
    }
    private static void setIfPresent(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value != null) {
            System.setProperty(key, value);
        }
    }
}