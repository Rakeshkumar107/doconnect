package com.wipro.cp.doconnect.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.cp.doconnect.dto.QuestionRequestDTO;
import com.wipro.cp.doconnect.dto.QuestionResponseDTO;
import com.wipro.cp.doconnect.dto.QuestionUpdateDTO;
import com.wipro.cp.doconnect.dto.StatusDTO;

@SpringBootTest
class QuestionServiceImpTest {
	
	@Autowired
	IQuestionService questionService;

	@Test
	void testGetAllQuestions() {
		StatusDTO<List<QuestionResponseDTO>> reponse = questionService.getAllQuestions("approved", null, "programming");
		assertNotNull(reponse);
	}

	@Test
	void testGetQuestionById() {
		StatusDTO<QuestionResponseDTO> response = questionService.getQuestionById(3L);
		assertNotNull(response);
	}

	@Test
	void testCreateQuestion() {
		QuestionRequestDTO question = new QuestionRequestDTO();
		question.setQuestion("What is Autowired anotation?");
		question.setTopic("Programming");
		StatusDTO<QuestionResponseDTO> response = questionService.createQuestion(question, "user04");
	}

	@Test
	void testUpdateQuestion() {
		QuestionUpdateDTO updatedQuestion = new QuestionUpdateDTO();
		updatedQuestion.setIsApproved(true);
		StatusDTO<QuestionResponseDTO> response = questionService.updateQuestion(updatedQuestion, 10L, "admin01");
		assertNotNull(response);
	}

	@Test
	void testDeleteQuestionById() {
		StatusDTO<Boolean> reponse = questionService.deleteQuestionById(10L);
		assertNotNull(reponse);
	}

}
