package com.foodwagon.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;



    @Value("${user.mail}")
    private String useremail;  
  
  public void sendOtp(String toEmail, String otp) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(useremail);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your FoodWagon Password");

            String htmlContent = buildOtpHtml(otp);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildOtpHtml(String otp) {
        return """
                <div style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:30px;">
                    <div style="max-width:600px; margin:auto; background:white; border-radius:10px; padding:30px; box-shadow:0 4px 10px rgba(0,0,0,0.05);">

                        <h2 style="color:#ff6b00; text-align:center;">
                            🚚 FoodWagon
                        </h2>

                        <p>Hello,</p>

                        <p>
                            We received a request to reset your password.
                        </p>

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
                            If you didn’t request this password reset, you can safely ignore this email.
                        </p>

                        <hr style="margin:25px 0; border:none; border-top:1px solid #eee;" />

                        <p style="text-align:center; font-size:12px; color:#aaa;">
                            © 2026 FoodWagon. All rights reserved.
                        </p>

                    </div>
                </div>
                """;
    }
}