package com.wipro.cp.doconnect.service;

import java.util.List;

import com.wipro.cp.doconnect.dto.QuestionRequestDTO;
import com.wipro.cp.doconnect.dto.QuestionResponseDTO;
import com.wipro.cp.doconnect.dto.QuestionUpdateDTO;
import com.wipro.cp.doconnect.dto.StatusDTO;

public interface IQuestionService {
	
	public StatusDTO<List<QuestionResponseDTO>> getAllQuestions(String status, String search, String topic);
	public StatusDTO<QuestionResponseDTO> getQuestionById(Long questionId);
	public StatusDTO<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO, String postedBy);
	public StatusDTO<QuestionResponseDTO> updateQuestion(QuestionUpdateDTO questionUpdateDTO, Long questionId, String approvedBy);
	public StatusDTO<Boolean> deleteQuestionById(Long questionId);

}
