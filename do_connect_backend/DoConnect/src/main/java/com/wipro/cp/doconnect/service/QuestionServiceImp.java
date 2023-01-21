package com.wipro.cp.doconnect.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wipro.cp.doconnect.dto.EmailDTO;
import com.wipro.cp.doconnect.dto.QuestionRequestDTO;
import com.wipro.cp.doconnect.dto.QuestionResponseDTO;
import com.wipro.cp.doconnect.dto.QuestionUpdateDTO;
import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.entity.Question;
import com.wipro.cp.doconnect.repository.QuestionRepository;
import com.wipro.cp.doconnect.repository.UserRepository;
import com.wipro.cp.doconnect.util.Utilities;

@Service
public class QuestionServiceImp implements IQuestionService {
	
	@Value("${enable-notification-emails:false}")
	private boolean enableNotificationEmails;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailServiceImp emailServiceImp;
	
	@Autowired
	private Utilities utilities;
	
	@Override
	public StatusDTO<List<QuestionResponseDTO>> getAllQuestions(String status, String search, String topic) {
		if (search == null && topic == null) {
			if (status.equalsIgnoreCase("all")) {
				return new StatusDTO<List<QuestionResponseDTO>>("", true, utilities.convertQuestionListToQuestionResponseDTOList(questionRepository.findAll()));
			}
			else if (status.equalsIgnoreCase("approved")) {
				return new StatusDTO<List<QuestionResponseDTO>>("", true, utilities.convertQuestionListToQuestionResponseDTOList(questionRepository.findByIsApprovedTrue()));
			}
			else if (status.equalsIgnoreCase("unapproved")) {
				return new StatusDTO<List<QuestionResponseDTO>>("", true, utilities.convertQuestionListToQuestionResponseDTOList(questionRepository.findByIsApprovedFalse()));
			}
			else {
				return new StatusDTO<List<QuestionResponseDTO>>("Provided invalid status. Should be one of 'all', 'approved' or 'unapproved'.", false, null);
			}
		}
		else if (search != null && topic == null) {
			if (status.equalsIgnoreCase("approved")) {
				return new StatusDTO<List<QuestionResponseDTO>>("", true, utilities.convertQuestionListToQuestionResponseDTOList(questionRepository.findByQuestionContainingIgnoreCaseAndIsApprovedTrue(search)));
			}
			else {
				return new StatusDTO<List<QuestionResponseDTO>>("Question search only works with 'approved' status.", false, null);
			}
		}
		else if (search == null && topic != null) {
			if (status.equalsIgnoreCase("approved")) {
				return new StatusDTO<List<QuestionResponseDTO>>("", true, utilities.convertQuestionListToQuestionResponseDTOList(questionRepository.findByTopicContainingIgnoreCaseAndIsApprovedTrue(topic)));
			}
			else {
				return new StatusDTO<List<QuestionResponseDTO>>("Question search only works with 'approved' status.", false, null);
			}
		}
		else {
			if (status.equalsIgnoreCase("approved")) {
				return new StatusDTO<List<QuestionResponseDTO>>("", true, utilities.convertQuestionListToQuestionResponseDTOList(questionRepository.findByTopicContainingIgnoreCaseAndQuestionContainingIgnoreCaseAndIsApprovedTrue(topic, search)));
			}
			else {
				return new StatusDTO<List<QuestionResponseDTO>>("Question search only works with 'approved' status.", false, null);
			}
		}
	}

	@Override
	public StatusDTO<QuestionResponseDTO> getQuestionById(Long questionId) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);
		if (optionalQuestion.isEmpty()) {
			return new StatusDTO<QuestionResponseDTO>("Question with ID " + questionId + " does not exist.", false, null);
		}
		return new StatusDTO<QuestionResponseDTO>("", true, utilities.convertQuestionToQuestionResponseDTO(optionalQuestion.get()));
	}

	@Override
	public StatusDTO<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionRequestDTO, String postedBy) {
		Question question = new Question(questionRequestDTO.getQuestion(), questionRequestDTO.getTopic(), questionRequestDTO.getImages(), postedBy);
		Question savedQuestion = questionRepository.save(question);
		if (enableNotificationEmails) {
			String[] recipients = utilities.getUserEmails(userRepository.findByIsAdminTrue());
			String subject = "Action Required: Approval needed for newly added question";
			String body = "Dear Admin,\n\nA new question has been added to DoConnect application. Please visit the application to either approve or delete the newly added question.\n\nDoConnect Bot\n\n\n\n\n\nAuto generated email. Please do not reply.";
			emailServiceImp.sendNotificationEmail(new EmailDTO(recipients, subject, body));
		}
		return new StatusDTO<QuestionResponseDTO>("", true, utilities.convertQuestionToQuestionResponseDTO(savedQuestion));
	}
	
	@Override
	public StatusDTO<QuestionResponseDTO> updateQuestion(QuestionUpdateDTO questionUpdateDTO, Long questionId, String approvedBy) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);
		if (optionalQuestion.isEmpty()) {
			return new StatusDTO<QuestionResponseDTO>("Question with ID " + questionId + " does not exist.", false, null);
		}
		Question question = optionalQuestion.get();
		question.setIsApproved(questionUpdateDTO.getIsApproved());
		question.setApprovedBy(approvedBy);
		return new StatusDTO<QuestionResponseDTO>("", true, utilities.convertQuestionToQuestionResponseDTO(questionRepository.save(question)));
	}
	
	@Override
	public StatusDTO<Boolean> deleteQuestionById(Long questionId) {
		Optional<Question> optionalQuestion = questionRepository.findById(questionId);
		if (optionalQuestion.isEmpty()) {
			return new StatusDTO<Boolean>("Question with id " + questionId + " does not exist.", false, null);
		}
		Question question = optionalQuestion.get();
		if (question.getIsApproved()) {
			return new StatusDTO<Boolean>("Cannot delete an approved question.", false, false);
		}
		questionRepository.delete(question);
		return new StatusDTO<Boolean>("", true, true);
	}

}
