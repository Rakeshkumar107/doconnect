package com.wipro.cp.doconnect.dto;

public class ChatMessageRequestDTO {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public ChatMessageRequestDTO(String message) {
		super();
		this.message = message;
	}

	public ChatMessageRequestDTO() {
		super();
	}

}
