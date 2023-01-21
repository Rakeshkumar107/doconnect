package com.wipro.cp.doconnect.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.dto.UserRegisterDTO;
import com.wipro.cp.doconnect.dto.UserResponseDTO;
import com.wipro.cp.doconnect.dto.UserUpdateDTO;
import com.wipro.cp.doconnect.service.UserServiceImp;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	@Autowired
	private UserServiceImp userServiceImp;
	
	@GetMapping("/users")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		return ResponseEntity.ok(userServiceImp.getAllUsers());
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable @Min(1) Long id) {
		StatusDTO<UserResponseDTO> userStatus = userServiceImp.getUserById(id);
		if (!userStatus.getIsValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
		StatusDTO<UserResponseDTO> userStatus = userServiceImp.createUser(userRegisterDTO);
		if (!userStatus.getIsValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(userStatus.getObject());
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO, @PathVariable Long id) {
		StatusDTO<UserResponseDTO> userStatus = userServiceImp.updateUser(userUpdateDTO, id);
		if (!userStatus.getIsValid()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatus.getStatusMessage());
		}
		return ResponseEntity.ok(userStatus.getObject());
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable @Min(1) Long id) {
		boolean deleted = userServiceImp.deleteUserById(id);
		if (deleted) {
			return ResponseEntity.ok("User deleted successfully.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " does not exist.");
	}

	@DeleteMapping("/users")
	public ResponseEntity<?> deleteAllUser() {
		userServiceImp.deleteAllUsers();
		return ResponseEntity.ok("All users deleted successfully.");
	}

}
