package com.wipro.cp.doconnect.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wipro.cp.doconnect.entity.LogoutToken;

@SpringBootTest
class LogoutTokenServiceImpTest {

	@Autowired
	ILogoutTokenService service;

	@Test
	void testCheckIfTokenExists() {
		boolean response = service.checkIfTokenExists("$2a$10$uZR8uoZTB76xcr6/F2ibRubV288fXkrZnyW78zNhdsW3zBxefALwe");
		assertNotNull(response);
	}

	@Test
	void testGetAllTokens() {
		List<LogoutToken> response = service.getAllTokens();
		assertNotNull(response);
	}

}
