package com.wipro.cp.doconnect.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.dto.UserRegisterDTO;
import com.wipro.cp.doconnect.dto.UserResponseDTO;
import com.wipro.cp.doconnect.dto.UserUpdateDTO;

@SpringBootTest
class UserServiceImpTest {
	
	@Autowired
	IUserService userService;

	@Test
	void testCreateUser() {
		UserRegisterDTO user = new UserRegisterDTO();
		user.setUsername("testuser");
		user.setEmail("testuser@gmail.com");
		user.setName("Test User");
		user.setIsAdmin(false);
		user.setPassword("testuser12345");
		StatusDTO<UserResponseDTO> response = userService.createUser(user);
		assertNotNull(response);
	}

	@Test
	void testGetAllUsers() {
		List<UserResponseDTO> response= userService.getAllUsers();
		assertNotNull(response);
	}

	@Test
	void testGetUserById() {
		StatusDTO<UserResponseDTO> response = userService.getUserById(1L);
		assertNotNull(response);
	}

	@Test
	void testUpdateUser() {
		UserUpdateDTO user = new UserUpdateDTO();
		user.setName("Test User Updated");
		user.setIsAdmin(false);
		StatusDTO<UserResponseDTO> response = userService.updateUser(user, 6L);
		assertNotNull(response);
	}

	@Test
	void testDeleteUserById() {
		boolean response = userService.deleteUserById(6L);
		assertNotNull(response);
	}

}
