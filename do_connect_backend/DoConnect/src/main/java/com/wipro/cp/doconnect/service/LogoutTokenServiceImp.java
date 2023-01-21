package com.wipro.cp.doconnect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.cp.doconnect.entity.LogoutToken;
import com.wipro.cp.doconnect.repository.LogoutTokenRepository;

@Service
public class LogoutTokenServiceImp implements ILogoutTokenService {
	
	@Autowired
	LogoutTokenRepository logoutTokenRepository;
	
	@Override
	public boolean checkIfTokenExists(String token) {
		return logoutTokenRepository.existsByToken(token);
	}
	
	@Override
	public LogoutToken createToken(LogoutToken logoutToken) {
		return logoutTokenRepository.save(logoutToken);
	}

	@Override
	public List<LogoutToken> createTokens(List<LogoutToken> logoutTokenList) {
		return logoutTokenRepository.saveAllAndFlush(logoutTokenList);
	}
	
	@Override
	public List<LogoutToken> getAllTokens() {
		return logoutTokenRepository.findAll();
	}

	@Override
	public void deleteAllTokens() {
		logoutTokenRepository.deleteAllInBatch();
	}

}
