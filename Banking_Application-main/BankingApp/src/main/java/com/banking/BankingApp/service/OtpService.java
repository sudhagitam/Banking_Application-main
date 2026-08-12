package com.banking.BankingApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

   private  JavaMailSender  javaMailSender;

    public void sendOtp(String email,String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
        String subject = "\"Otp for password reset\"";
        String text = "Your OTP is: " + otp + "\nIt will expire in 15 minutes.";

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);
        mimeMessageHelper.setTo(email);

        try{
            javaMailSender.send(mimeMessage);
        }
        catch (MailException e){
            throw new MailSendException(e.getMessage());
        }
    }
}
