package com.james.springbootmall.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(to); // 寄件人

        mailSender.send(message);
        System.out.println("郵件已發送至：" + to);
    }

    public void createOrder(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject); //標題
        message.setText(text);
        message.setFrom(email); // 寄件人

        mailSender.send(message);
        System.out.println("郵件已發送至：" + email);
    }

}
