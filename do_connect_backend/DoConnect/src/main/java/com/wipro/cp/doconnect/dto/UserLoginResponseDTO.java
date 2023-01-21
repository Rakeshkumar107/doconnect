package com.wipro.cp.doconnect.dto;

import java.io.Serializable;

public class UserLoginResponseDTO implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String tokenType;

	public UserLoginResponseDTO(String jwttoken, String tokenType) {
		this.jwttoken = jwttoken;
		this.tokenType = tokenType;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public String getTokenType() {
		return tokenType;
	}

}
