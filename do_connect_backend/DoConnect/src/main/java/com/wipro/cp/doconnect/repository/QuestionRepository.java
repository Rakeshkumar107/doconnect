package com.wipro.cp.doconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.cp.doconnect.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	boolean existsById(Long id);
	
	List<Question> findByIsApprovedTrue();
	List<Question> findByIsApprovedFalse();
	
	List<Question> findByQuestionContainingIgnoreCaseAndIsApprovedTrue(String question);
	List<Question> findByTopicContainingIgnoreCaseAndIsApprovedTrue(String topic);
	List<Question> findByTopicContainingIgnoreCaseAndQuestionContainingIgnoreCaseAndIsApprovedTrue(String topic, String question);
}
