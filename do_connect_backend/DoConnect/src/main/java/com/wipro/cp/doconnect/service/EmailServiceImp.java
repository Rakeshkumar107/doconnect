package com.wipro.cp.doconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wipro.cp.doconnect.dto.EmailDTO;

@Service
public class EmailServiceImp implements IEmailService {

	private final Logger log = LoggerFactory.getLogger(EmailServiceImp.class);

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public boolean sendNotificationEmail(EmailDTO emailDTO) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(sender);
			simpleMailMessage.setTo(emailDTO.getRecipients());
			simpleMailMessage.setSubject(emailDTO.getSubject());
			simpleMailMessage.setText(emailDTO.getBody());
			javaMailSender.send(simpleMailMessage);
			log.info("Notification email sent to " + emailDTO.getRecipients().toString());
			return true;
		}
		catch (Exception e) {
			log.warn("Failed to send Notification email. Details: " + e.toString());
			return false;
		}
	}

}
