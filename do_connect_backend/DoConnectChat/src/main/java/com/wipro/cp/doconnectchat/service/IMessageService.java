package com.wipro.cp.doconnectchat.service;

import java.util.List;

import com.wipro.cp.doconnectchat.dto.MessageRequestDTO;
import com.wipro.cp.doconnectchat.dto.MessageResponseDTO;

public interface IMessageService {
	
	public List<MessageResponseDTO> getAllMessages();
	
	public boolean createMessage(MessageRequestDTO messageRequestDTO);

}
