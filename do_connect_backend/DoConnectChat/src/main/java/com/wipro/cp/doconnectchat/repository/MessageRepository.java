package com.wipro.cp.doconnectchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.cp.doconnectchat.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	
	List<Message> findAllByOrderByPostedAtAsc();

}
