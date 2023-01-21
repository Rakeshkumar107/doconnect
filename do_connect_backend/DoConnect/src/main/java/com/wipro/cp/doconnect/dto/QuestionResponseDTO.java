package com.wipro.cp.doconnect.dto;

import java.util.Date;
import java.util.List;

public class QuestionResponseDTO {
	
	private long id;
	private String question;
	private String topic;
	private List<String> images;
	private String postedBy;
	private Date postedAt;
	private String approvedBy;
	private boolean isApproved;

	public long getId() {
		return id;
	}
	public String getQuestion() {
		return question;
	}
	public String getTopic() {
		return topic;
	}
	public List<String> getImages() {
		return images;
	}
	public String getPostedBy() {
		return postedBy;
	}
	public Date getPostedAt() {
		return postedAt;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public boolean getIsApproved() {
		return isApproved;
	}

	public QuestionResponseDTO(long id, String question, String topic, List<String> images, String postedBy,
			Date postedAt, String approvedBy, boolean isApproved) {
		super();
		this.id = id;
		this.question = question;
		this.topic = topic;
		this.images = images;
		this.postedBy = postedBy;
		this.postedAt = postedAt;
		this.approvedBy = approvedBy;
		this.isApproved = isApproved;
	}

	public QuestionResponseDTO() {
		super();
	}

}
