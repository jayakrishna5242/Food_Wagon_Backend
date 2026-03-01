package com.foodwagon.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${user.mail}")
    private String userEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendOtp(String toEmail, String otp) {

        String url = "https://api.brevo.com/v3/smtp/email";

        Map<String, Object> requestBody = Map.of(
                "sender", Map.of(
                        "name", "FoodWagon",
                        "email", userEmail
                ),
                "to", new Object[]{
                        Map.of("email", toEmail)
                },
                "subject", "Reset Your FoodWagon Password",
                "htmlContent", buildOtpHtml(otp)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Brevo API", e);
        }
    }

    private String buildOtpHtml(String otp) {
        return """
                <div style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:30px;">
                    <div style="max-width:600px; margin:auto; background:white; border-radius:10px; padding:30px;">

                        <h2 style="color:#ff6b00; text-align:center;">
                            🚚 FoodWagon
                        </h2>

                        <p>Hello,</p>

                        <p>We received a request to reset your password.</p>

                        <div style="text-align:center; margin:30px 0;">
                            <span style="
                                font-size:28px;
                                font-weight:bold;
                                letter-spacing:5px;
                                background:#ff6b00;
                                color:white;
                                padding:12px 24px;
                                border-radius:8px;">
                                """ + otp + """
                            </span>
                        </div>

                        <p style="text-align:center;">
                            This OTP is valid for <b>5 minutes</b>.
                        </p>

                        <p style="font-size:13px; color:#777;">
                            If you didn’t request this password reset, ignore this email.
                        </p>

                        <hr style="margin:25px 0;" />

                        <p style="text-align:center; font-size:12px; color:#aaa;">
                            © 2026 FoodWagon
                        </p>

                    </div>
                </div>
                """;
    }
}