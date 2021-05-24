package com.qrlo.qrloservicecore.service;

import com.qrlo.qrloservicecore.model.BusinessCard;
import com.qrlo.qrloservicecore.model.User;
import com.qrlo.qrloservicecore.config.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-13
 */
@Slf4j
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final TemplateEngine emailTemplateEngine;
    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender javaMailSender, JwtTokenProvider jwtTokenProvider, TemplateEngine emailTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailTemplateEngine = emailTemplateEngine;
    }

    public Mono<Boolean> sendAccountVerificationEmail(User user) {
        return Mono.fromCallable(() -> {
            try {
                final MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
                final MimeMessageHelper message = new MimeMessageHelper(mimeMailMessage, true, "UTF-8");
                message.setFrom("qrlo.developer@gmail.com");
                message.setTo(user.getEmail());
                message.setSubject("[큐알로] 이메일 계정 연동 인증");
                String token = jwtTokenProvider.generateVerificationToken(user);
                String verificationUrl = "http://192.168.2.10:3000/api/v1/verification/accounts/" + token;

                final Context context = new Context();
                context.setVariable("verificationUrl", verificationUrl);

                String htmlContent = emailTemplateEngine.process("html/verification/account", context);
                message.setText(htmlContent, true);
                javaMailSender.send(mimeMailMessage);
                return true;
            } catch (MessagingException e) {
                log.error("Error Occurred during sending email", e);
                throw e;
            }
        });
    }

    public Mono<Void> sendBusinessCardVerificationEmail(User user, BusinessCard businessCard) {
        return Mono.fromCallable(() -> {
            try {
                log.info("Sending email for verification to User: {} for Business Card: {}", user.toString(), businessCard.toString());
                final MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
                final MimeMessageHelper message = new MimeMessageHelper(mimeMailMessage, true, "UTF-8");
                message.setFrom(sender);
                message.setTo(businessCard.getEmail());
                message.setSubject("[큐알로] 비즈니스 명함 이메일 계정 연동 인증");
                String token = jwtTokenProvider.generateVerificationToken(businessCard);
                String verificationUrl = "http://192.168.2.10:3000/api/v1/verification/businesscards/" + token;

                final Context context = new Context();
                context.setVariable("userLastName", user.getLastName());
                context.setVariable("userFirstName", user.getFirstName());
                context.setVariable("verificationUrl", verificationUrl);

                String htmlContent = emailTemplateEngine.process("html/verification/businesscard", context);
                message.setText(htmlContent, true);
                javaMailSender.send(mimeMailMessage);
                return true;
            } catch (MessagingException e) {
                log.error("Error Occurred during sending email", e);
                throw e;
            }
        }).then();
    }
}
