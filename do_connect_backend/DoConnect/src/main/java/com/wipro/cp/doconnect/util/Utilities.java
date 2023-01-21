package com.wipro.cp.doconnect.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wipro.cp.doconnect.component.JwtTokenUtilityComponent;
import com.wipro.cp.doconnect.dto.AnswerResponseDTO;
import com.wipro.cp.doconnect.dto.QuestionResponseDTO;
import com.wipro.cp.doconnect.entity.Answer;
import com.wipro.cp.doconnect.entity.Question;
import com.wipro.cp.doconnect.entity.User;

@Component
public class Utilities {
	
	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;
	
	public String[] getUserEmails(List<User> userList) {
		String[] array = new String[userList.size()];
		for (int i = 0; i < userList.size(); i++) {
			array[i] = userList.get(i).getEmail();
		}
		return array;
	}
	
	public QuestionResponseDTO convertQuestionToQuestionResponseDTO(Question question) {
		return new QuestionResponseDTO(question.getId(), question.getQuestion(), question.getTopic(), question.getImages(), question.getPostedBy(), question.getPostedAt(), question.getApprovedBy(), question.getIsApproved());
	}
	
	public List<QuestionResponseDTO> convertQuestionListToQuestionResponseDTOList(List<Question> questionList) {
		return questionList.stream().map(question -> convertQuestionToQuestionResponseDTO(question)).collect(Collectors.toList());
	}
	
	public AnswerResponseDTO convertAnswerToAnswerResponseDTO(Answer answer) {
		return new AnswerResponseDTO(answer.getId(), answer.getAnswer(), answer.getImages(), answer.getPostedBy(), answer.getPostedAt(), answer.getApprovedBy(), answer.getIsApproved(), convertQuestionToQuestionResponseDTO(answer.getQuestion()));
	}
	
	public List<AnswerResponseDTO> convertAnswerListToAnswerResponseDTOList(List<Answer> answerList) {
		return answerList.stream().map(answer -> convertAnswerToAnswerResponseDTO(answer)).collect(Collectors.toList());
	}
	
	public String getUsernameFromAuthorizationHeader(String authorizationHeader) {
		String jwtToken = authorizationHeader.substring(7);
		return jwtTokenUtilityComponent.getUsernameFromToken(jwtToken);
	}

}
