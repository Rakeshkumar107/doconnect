package com.wipro.cp.doconnectchat.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.cp.doconnectchat.dto.MessageRequestDTO;
import com.wipro.cp.doconnectchat.dto.MessageResponseDTO;
import com.wipro.cp.doconnectchat.service.MessageServiceImp;

@RestController
@RequestMapping("/api/v1")
public class MessageController {
	
	@Autowired
	private MessageServiceImp messageServiceImp;
	
	@GetMapping("/messages")
	public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
		return ResponseEntity.ok(messageServiceImp.getAllMessages());
	}
	
	@PostMapping("/messages")
	public ResponseEntity<?> createMessage(@Valid @RequestBody MessageRequestDTO messageRequestDTO) {
		boolean status = messageServiceImp.createMessage(messageRequestDTO);
		if (status) {
			return ResponseEntity.status(HttpStatus.CREATED).body(messageServiceImp.getAllMessages());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send message.");
		}
	}

}
