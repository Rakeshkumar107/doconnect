package com.wipro.cp.doconnectchat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.cp.doconnectchat.dto.MessageRequestDTO;
import com.wipro.cp.doconnectchat.dto.MessageResponseDTO;
import com.wipro.cp.doconnectchat.entity.Message;
import com.wipro.cp.doconnectchat.repository.MessageRepository;

@Service
public class MessageServiceImp implements IMessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	private MessageResponseDTO convertMessageToMessageResponseDTO(Message message) {
		return new MessageResponseDTO(message.getMessage(), message.getPostedAt(), message.getPostedBy());
	}

	@Override
	public List<MessageResponseDTO> getAllMessages() {
		return messageRepository.findAllByOrderByPostedAtAsc().stream().map(message -> convertMessageToMessageResponseDTO(message)).collect(Collectors.toList());
	}

	@Override
	public boolean createMessage(MessageRequestDTO messageRequestDTO) {
		try {
			messageRepository.save(new Message(messageRequestDTO.getMessage(), messageRequestDTO.getPostedBy()));
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}
