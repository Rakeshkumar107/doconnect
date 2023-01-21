package com.wipro.cp.doconnect.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.cp.doconnect.component.JwtTokenUtilityComponent;
import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.dto.UserLoginRequestDTO;
import com.wipro.cp.doconnect.dto.UserLoginResponseDTO;
import com.wipro.cp.doconnect.dto.UserRegisterDTO;
import com.wipro.cp.doconnect.dto.UserResponseDTO;
import com.wipro.cp.doconnect.entity.LogoutToken;
import com.wipro.cp.doconnect.service.LogoutTokenServiceImp;
import com.wipro.cp.doconnect.service.UserServiceImp;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;
	
	@Autowired
	private UserServiceImp userServiceImp;
	
	@Autowired
	private LogoutTokenServiceImp logoutTokenServiceImp;
	
	@GetMapping("/signout")
	public ResponseEntity<String> logoutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		if (authorizationHeader.startsWith("Bearer ")) {
			String jwtToken = authorizationHeader.substring(7);
			logoutTokenServiceImp.createToken(new LogoutToken(jwtToken));
			return ResponseEntity.ok("You have been successfully logged out.");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header does not begin with Bearer string.");
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
		StatusDTO<UserResponseDTO> userStatus = userServiceImp.createUser(userRegisterDTO);
		if (!userStatus.getIsValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userStatus.getStatusMessage());
		}
		UserResponseDTO userResponseDTO = userStatus.getObject();
		return ResponseEntity.ok(userResponseDTO.getUsername() + " has been registered successfully" + (userResponseDTO.getIsAdmin() ? " as an admin." : " as a user.") + " Proceed to login to get your authorization token.");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginRequestDTO userLoginRequestDTO) throws Exception {
		authenticate(userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword());
		final UserDetails userDetails = userServiceImp.loadUserByUsername(userLoginRequestDTO.getUsername());
		final String token = jwtTokenUtilityComponent.generateToken(userDetails);
		return ResponseEntity.ok(new UserLoginResponseDTO(token, "Bearer"));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
