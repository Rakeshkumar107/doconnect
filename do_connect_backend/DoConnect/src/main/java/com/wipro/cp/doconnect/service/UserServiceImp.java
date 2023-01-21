package com.wipro.cp.doconnect.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.dto.UserRegisterDTO;
import com.wipro.cp.doconnect.dto.UserResponseDTO;
import com.wipro.cp.doconnect.dto.UserUpdateDTO;
import com.wipro.cp.doconnect.entity.User;
import com.wipro.cp.doconnect.repository.UserRepository;

@Service
public class UserServiceImp implements IUserService, UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	private UserResponseDTO convertUserToUserResponseDTO(User user) {
		return new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getIsAdmin());
	}
	
	private StatusDTO<UserResponseDTO> convertOptionalUserToStatusDTOUserResponseDTO(Optional<User> optionalUser, String statusMessage) {
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserResponseDTO>(statusMessage, false, null);
		}
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(optionalUser.get()));
	}
	
	public StatusDTO<UserResponseDTO> getUserByUsername(String username) {
		return convertOptionalUserToStatusDTOUserResponseDTO(userRepository.findByUsername(username), "User with username " + username + " does not exist.");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		User user = optionalUser.get();
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

	@Override
	public StatusDTO<UserResponseDTO> createUser(UserRegisterDTO userRegisterDTO) {
		if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
			return new StatusDTO<UserResponseDTO>("User with username " + userRegisterDTO.getUsername() + " already exists. Please create user with different username.", false, null);
		}
		if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
			return new StatusDTO<UserResponseDTO>("User with email " + userRegisterDTO.getEmail() + " already exists. Please create user with different email.", false, null);
		}
		if (userRegisterDTO.getPassword().length() < 8) {
			return new StatusDTO<UserResponseDTO>("Password should contain at least 8 characters.", false, null);
		}
		User user = new User(userRegisterDTO.getUsername(), userRegisterDTO.getName(), bcryptEncoder.encode(userRegisterDTO.getPassword()), userRegisterDTO.getEmail(), userRegisterDTO.getIsAdmin());
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(userRepository.save(user)));
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		return userRepository.findAll().stream().map(user -> convertUserToUserResponseDTO(user)).collect(Collectors.toList());
	}

	@Override
	public StatusDTO<UserResponseDTO> getUserById(Long id) {
		return convertOptionalUserToStatusDTOUserResponseDTO(userRepository.findById(id), "User with ID " + id + " does not exist.");
	}

	@Override
	public StatusDTO<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO, Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			return new StatusDTO<UserResponseDTO>("User with ID " + id + " does not exist.", false, null);
		}
		User user = optionalUser.get();
		user.setIsAdmin(userUpdateDTO.getIsAdmin());
		user.setName(userUpdateDTO.getName());
		return new StatusDTO<UserResponseDTO>("", true, convertUserToUserResponseDTO(userRepository.save(user)));
	}

	@Override
	public boolean deleteUserById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}

	@Override
	public void deleteAllUsers() {
		userRepository.deleteAllInBatch();
	}

}
