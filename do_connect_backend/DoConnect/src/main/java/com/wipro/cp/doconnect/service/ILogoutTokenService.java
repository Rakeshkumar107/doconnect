package com.wipro.cp.doconnect.service;

import java.util.List;

import com.wipro.cp.doconnect.entity.LogoutToken;

public interface ILogoutTokenService {
	
	public boolean checkIfTokenExists(String token);
	public LogoutToken createToken(LogoutToken logoutToken);
	public List<LogoutToken> createTokens(List<LogoutToken> logoutTokenList);
	public List<LogoutToken> getAllTokens();
	public void deleteAllTokens();

}
