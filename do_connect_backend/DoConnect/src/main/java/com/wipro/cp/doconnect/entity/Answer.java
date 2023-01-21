package com.wipro.cp.doconnect.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.cp.doconnect.util.ListToStringConverter;

@Entity
@Table(name="answers")
public class Answer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty
	@Size(max = 1024)
	private String answer;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Question question;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
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

	public long getId() {
		return id;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Answer(@NotNull @NotEmpty String answer, List<String> images, @NotNull @NotEmpty String postedBy, Question question) {
		super();
		this.answer = answer;
		this.images = images;
		this.postedBy = postedBy;
		this.approvedBy = null;
		this.isApproved = false;
		this.question = question;
	}

	public Answer() {
		super();
	}

}
