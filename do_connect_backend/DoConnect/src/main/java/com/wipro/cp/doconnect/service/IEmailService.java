package com.wipro.cp.doconnect.service;

import com.wipro.cp.doconnect.dto.EmailDTO;

public interface IEmailService {
	
	boolean sendNotificationEmail(EmailDTO emailDTO);
	
}
