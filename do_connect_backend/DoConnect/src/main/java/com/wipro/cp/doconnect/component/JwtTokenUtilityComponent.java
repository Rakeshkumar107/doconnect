package com.wipro.cp.doconnect.component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.dto.UserResponseDTO;
import com.wipro.cp.doconnect.service.LogoutTokenServiceImp;
import com.wipro.cp.doconnect.service.UserServiceImp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtilityComponent implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	@Value("${jwt.token-validity-time}")
	private int JWT_TOKEN_VALIDITY;

	@Value("${jwt.secret}")
	private String secret;
	
	@Autowired
	private LogoutTokenServiceImp logoutTokenServiceImp;
	
	@Autowired
	private UserServiceImp userServiceImp;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		StatusDTO<UserResponseDTO> userStatus = userServiceImp.getUserByUsername(userDetails.getUsername());
		if (userStatus.getIsValid()) {
			UserResponseDTO user = userStatus.getObject();
			claims.put("isAdmin", user.getIsAdmin());
			claims.put("email", user.getEmail());
			claims.put("name", user.getName());
		}
		return doGenerateToken(claims, userDetails.getUsername());
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1) compaction of the JWT to a URL-safe string 
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	//validate token
	//First check if the token is present in the logout tokens list, if it exists, then token is invalid
	//If token is still valid then get user name from token and check if it is same as in user details and the token is not expired
	public Boolean validateToken(String token, UserDetails userDetails) {
		if (logoutTokenServiceImp.checkIfTokenExists(token)) {
			return false;
		}
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
