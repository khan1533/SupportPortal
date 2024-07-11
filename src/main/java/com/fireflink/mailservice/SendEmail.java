package com.fireflink.mailservice;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SendEmail {


    private final SpringMailSender springMailSender;

    public void createMail(String[] user1, String subject, String text) throws MessagingException {
        EmailModel emailModel=new EmailModel();
        emailModel.setTo(user1);
        emailModel.setSubject(subject);
        emailModel.setText(text);
        springMailSender.sendMail(emailModel);


    }
}
