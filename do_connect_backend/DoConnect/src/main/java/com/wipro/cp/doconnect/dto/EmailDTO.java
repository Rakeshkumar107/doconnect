package com.wipro.cp.doconnect.dto;

public class EmailDTO {

	private String[] recipients;
	private String subject;
	private String body;

	public String[] getRecipients() {
		return recipients;
	}
	public String getSubject() {
		return subject;
	}
	public String getBody() {
		return body;
	}

	public EmailDTO(String[] recipients, String subject, String body) {
		super();
		this.recipients = recipients;
		this.subject = subject;
		this.body = body;
	}

	public EmailDTO() {
		super();
	}

}
