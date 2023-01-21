package com.wipro.cp.doconnect.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.cp.doconnect.dto.AnswerRequestDTO;
import com.wipro.cp.doconnect.dto.AnswerResponseDTO;
import com.wipro.cp.doconnect.dto.AnswerUpdateDTO;
import com.wipro.cp.doconnect.dto.StatusDTO;

@SpringBootTest
class AnswerServiceImpTest {

	@Autowired
	IAnswerService service;

	@Test
	void testGetAllAnswers() {
		StatusDTO<List<AnswerResponseDTO>> response = service.getAllAnswers("all");
		assertNotNull(response);
	}

	@Test
	void testGetAllAnswersForQuestionId() {
		StatusDTO<List<AnswerResponseDTO>> response = service.getAllAnswersForQuestionId(1l, "all");
		assertNotNull(response);
	}

	@Test
	void testCreateAnswerForQuestionId() {
		List<String> imglist = new ArrayList<>();
		imglist.add("01.png");
		AnswerRequestDTO ans = new  AnswerRequestDTO("Good", imglist);
		StatusDTO<AnswerResponseDTO> ansresponse = service.createAnswerForQuestionId(1l, ans, "TestUser01");
		assertNotNull(ansresponse);
	}

	@Test
	void testUpdateAnswerForQuestionId() {
		AnswerUpdateDTO ansupdate = new AnswerUpdateDTO(false);
		StatusDTO<AnswerResponseDTO> response = service.updateAnswerForQuestionId(1l, 1l, ansupdate, "TestAdmin01");
		assertNotNull(response);
	}

	@Test
	void testDeleteAnswerForQuestionById() {
		StatusDTO<Boolean> response = service.deleteAnswerForQuestionById(1l, 2l);
		assertNotNull(response);
	}

}
