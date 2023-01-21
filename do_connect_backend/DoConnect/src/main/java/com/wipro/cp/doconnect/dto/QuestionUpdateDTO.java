package com.wipro.cp.doconnect.dto;

public class QuestionUpdateDTO {

	private boolean isApproved;

	public boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public QuestionUpdateDTO(boolean isApproved) {
		super();
		this.isApproved = isApproved;
	}

	public QuestionUpdateDTO() {
		super();
	}
	
}
