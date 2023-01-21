package com.wipro.cp.doconnect.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.wipro.cp.doconnect.component.JwtTokenUtilityComponent;
import com.wipro.cp.doconnect.entity.LogoutToken;
import com.wipro.cp.doconnect.service.LogoutTokenServiceImp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;

@Configuration
@EnableScheduling
public class LogoutTokenSchedulerConfig {

	private final Logger log = LoggerFactory.getLogger(LogoutTokenSchedulerConfig.class);
	
	@Autowired
	private LogoutTokenServiceImp logoutTokenServiceImp;
	
	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;
	
	@Scheduled(fixedDelay = 15 * 60 * 1000) // 15 Minutes
	public void deleteExpiredJwtTokens() {
		log.info("deleteExpiredJwtTokens scheduled job started...");
	    List<LogoutToken> logoutTokenList = logoutTokenServiceImp.getAllTokens();
	    logoutTokenServiceImp.deleteAllTokens();
	    List<LogoutToken> validLogoutTokenList = new ArrayList<>();
	    for (LogoutToken logoutToken : logoutTokenList) {
	    	try {
	    		if (jwtTokenUtilityComponent.isTokenExpired(logoutToken.getToken())) {
	    			log.info("JWT Token " + logoutToken.getToken() + " removed from logout_token table.");
	    		} else {
	    			validLogoutTokenList.add(logoutToken);
	    		}
	    	} catch (ExpiredJwtException e) {
	    		log.warn("Exception in deleteExpiredJwtTokens - " + e.toString());
	    	}
	    }
	    logoutTokenServiceImp.createTokens(validLogoutTokenList);
	    log.info("deleteExpiredJwtTokens scheduled job finished.");
	}

}
