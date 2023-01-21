package com.wipro.cp.doconnect.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.wipro.cp.doconnect.util.ListToStringConverter;

@Entity
@Table(name="questions")
public class Question {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty
	@Size(max = 1024)
	private String question;
	
	@NotNull
	@NotEmpty
	private String topic;
	
	@Column
	@Convert(converter = ListToStringConverter.class)
	private List<String> images;
	
	@NotNull
	@NotEmpty
	private String postedBy;
	
	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private Date postedAt;
	
	private String approvedBy;
	
	private boolean isApproved;

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

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public long getId() {
		return id;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public Question(@NotNull @NotEmpty String question, @NotNull @NotEmpty String topic, List<String> images,
			@NotNull @NotEmpty String postedBy) {
		super();
		this.question = question;
		this.topic = topic;
		this.images = images;
		this.postedBy = postedBy;
		this.approvedBy = null;
		this.isApproved = false;
	}

	public Question() {
		super();
	}
	
}
