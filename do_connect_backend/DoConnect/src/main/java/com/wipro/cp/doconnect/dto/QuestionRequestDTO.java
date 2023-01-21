package com.wipro.cp.doconnect.dto;

import java.util.List;

public class QuestionRequestDTO {
	
	private String question;
	private String topic;
	private List<String> images;

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}

	public QuestionRequestDTO(String question, String topic, List<String> images) {
		super();
		this.question = question;
		this.topic = topic;
		this.images = images;
	}

	public QuestionRequestDTO() {
		super();
	}

}
