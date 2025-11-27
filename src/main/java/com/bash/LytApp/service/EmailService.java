package com.bash.LytApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendVerificationEmail(String toEmail, String name, String token) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("token", token);

            String htmlContent = templateEngine.process("email/verification-email", context);
            sendEmail(toEmail, "LightPay - Email Verification", htmlContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String username, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - Bill Management App");
            helper.setFrom("noreply@billmanager.com");

            String emailContent = createPasswordResetEmailContent(username, resetToken);
            helper.setText(emailContent, true);

            mailSender.send(message);
            System.out.println("Password reset email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    @Async
    public void sendPaymentConfirmation(String toEmail, String userName, String transactionId,
                                        Double amount, String paymentMethod, LocalDateTime paymentDate) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("transactionId", transactionId);
            context.setVariable("amount", amount);
            context.setVariable("paymentMethod", paymentMethod);
            context.setVariable("paymentDate", paymentDate);

            String htmlContent = templateEngine.process("email/payment-confirmation", context);
            sendEmail(toEmail, "LightPay - Payment Confirmation", htmlContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send payment confirmation email", e);
        }
    }


    @Async
    public void sendBillNotification(String toEmail, String userName, Double amount,
                                     LocalDateTime dueDate, String billId) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("amount", amount);
            context.setVariable("dueDate", dueDate);
            context.setVariable("billId", billId);

            String htmlContent = templateEngine.process("email/bill-notification", context);
            sendEmail(toEmail, "LightPay - New Bill Notification", htmlContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send bill notification email", e);
        }
    }


    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("noreply@lightpay.com");

        mailSender.send(message);
    }

    private String createPasswordResetEmailContent(String username, String resetToken) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("resetToken", resetToken);
        context.setVariable("resetLink", "http://localhost:63342/reset-password?token=" + resetToken);

        return templateEngine.process("password-reset-template", context);
    }

}
