package com.wipro.cp.doconnectchat.dto;

public class MessageRequestDTO {

	private String message;
	
	private String postedBy;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public MessageRequestDTO(String message, String postedBy) {
		super();
		this.message = message;
		this.postedBy = postedBy;
	}

	public MessageRequestDTO() {
		super();
	}

}
