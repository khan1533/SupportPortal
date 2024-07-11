package com.fireflink.mailservice;

import java.util.Date;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SpringMailSender {

	private final JavaMailSender javaMailSender;
	
	
	public void sendMail(EmailModel emailModel) throws MessagingException {
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message, true);
		helper.setTo(emailModel.getTo());
		helper.setSubject(emailModel.getSubject());
		helper.setText(emailModel.getText());
		helper.setSentDate(new Date());
		javaMailSender.send(message);
		
	}
}
