package com.wipro.cp.doconnect.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wipro.cp.doconnect.component.JwtTokenUtilityComponent;
import com.wipro.cp.doconnect.dto.StatusDTO;
import com.wipro.cp.doconnect.dto.UserResponseDTO;
import com.wipro.cp.doconnect.service.UserServiceImp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationInterceptor implements HandlerInterceptor {

	private final Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);
	
	@Autowired
	private UserServiceImp userServiceImp;

	@Autowired
	private JwtTokenUtilityComponent jwtTokenUtilityComponent;
	
	private String getParameterValue(Map<String, String[]> parameterMap, String key) {
		String[] value = parameterMap.getOrDefault(key, null);
		if ((value == null) || (value.length != 1) || value[0].length() == 0) {
			return null;
		}
		return value[0];
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String currentMethod = request.getMethod();
		final String currentURI = request.getRequestURI().toString();
		final Map<String, String[]> parameterMap = request.getParameterMap();
		if (currentURI.equalsIgnoreCase("/api/v1/questions") && HttpMethod.GET.matches(currentMethod)) {
			if (!parameterMap.containsKey("status") && !parameterMap.containsKey("search") && !parameterMap.containsKey("topic")) {
				return true;
			}
		}
        else if (currentURI.equalsIgnoreCase("/api/v1/register") || currentURI.equalsIgnoreCase("/api/v1/login")) {
        	if (HttpMethod.POST.matches(currentMethod)) {
        		return true;
        	}
        	response.sendError(HttpStatus.FORBIDDEN.value());
			return false;
        }
        final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtilityComponent.getUsernameFromToken(jwtToken);
			} catch (Exception e) {
				log.warn("Exception " + e.toString());
			}
		}
		if (username != null) {
			StatusDTO<UserResponseDTO> userStatus = userServiceImp.getUserByUsername(username);
			if (!userStatus.getIsValid()) {
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
			UserResponseDTO userResponseDTO = userStatus.getObject();
			boolean isUserAdmin = userResponseDTO.getIsAdmin();
			if (currentURI.equalsIgnoreCase("/api/v1/signout") && HttpMethod.GET.matches(currentMethod)) {
				return true;
			}
			else if (currentURI.equalsIgnoreCase("/api/v1/answers") && HttpMethod.GET.matches(currentMethod) && isUserAdmin) {
				return true;
			}
			else if (
				currentURI.equalsIgnoreCase("/api/v1/messages") &&
				(HttpMethod.GET.matches(currentMethod) || HttpMethod.POST.matches(currentMethod)) &&
				!isUserAdmin
			) {
				return true;
			}
			else if (
				currentURI.toLowerCase().startsWith("/api/v1/users") &&
				(HttpMethod.GET.matches(currentMethod) || HttpMethod.POST.matches(currentMethod) || HttpMethod.PUT.matches(currentMethod) || HttpMethod.DELETE.matches(currentMethod))
			) {
				if (isUserAdmin) {
					return true;
				}
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
			else if (
				currentURI.toLowerCase().startsWith("/api/v1/images") &&
				(HttpMethod.GET.matches(currentMethod) || HttpMethod.POST.matches(currentMethod)) && !isUserAdmin
			) {
				return true;
			}
			else if (currentURI.toLowerCase().startsWith("/api/v1/questions")) {
				String[] uriParts = currentURI.split("/");
				if (uriParts.length == 4) {
					if (uriParts[3].equalsIgnoreCase("questions")) {
						if (HttpMethod.POST.matches(currentMethod) && !isUserAdmin) {
							return true;
						}
						else if (HttpMethod.GET.matches(currentMethod)) {
							String statusValue = getParameterValue(parameterMap, "status");
							String searchValue = getParameterValue(parameterMap, "search");
							String topicValue = getParameterValue(parameterMap, "topic");
							if (topicValue == null && searchValue == null && statusValue == null && !isUserAdmin) {
								return true;
							}
							else if (topicValue == null && searchValue == null && statusValue != null) {
								if (statusValue.equalsIgnoreCase("approved")) {
									return true;
								}
								else if ((statusValue.equalsIgnoreCase("unapproved") || statusValue.equalsIgnoreCase("all")) && isUserAdmin) {
									return true;
								}
								else {
									response.sendError(HttpStatus.FORBIDDEN.value());
									return false;
								}
							}
							else if ((topicValue != null || searchValue != null) && statusValue == null) {
								if (!isUserAdmin) {
									return true;
								}
								else {
									response.sendError(HttpStatus.FORBIDDEN.value());
									return false;
								}
							}
							else {
								if (statusValue.equalsIgnoreCase("approved") && !isUserAdmin) {
									return true;
								}
								else {
									response.sendError(HttpStatus.FORBIDDEN.value());
									return false;
								}
							}
						}
						else {
							response.sendError(HttpStatus.FORBIDDEN.value());
							return false;
						}
					}
					else {
						response.sendError(HttpStatus.FORBIDDEN.value());
						return false;
					}
				}
				else if (uriParts.length == 5) {
					if (
						uriParts[3].equalsIgnoreCase("questions") &&
						(HttpMethod.PUT.matches(currentMethod) || HttpMethod.DELETE.matches(currentMethod)) &&
						isUserAdmin
					) {
						return true;
					}
					else if (uriParts[3].equalsIgnoreCase("questions") && HttpMethod.GET.matches(currentMethod)) {
						return true;
					}
					else{
						response.sendError(HttpStatus.FORBIDDEN.value());
						return false;
					}
				}
				else if (uriParts.length == 6) {
					if (uriParts[3].equalsIgnoreCase("questions") && uriParts[5].equalsIgnoreCase("answers")) {
						if (HttpMethod.POST.matches(currentMethod) && !isUserAdmin) {
							return true;
						}
						else if (HttpMethod.GET.matches(currentMethod)) {
							String statusValue = getParameterValue(parameterMap, "status");
							if (statusValue == null || statusValue.equalsIgnoreCase("approved")) {
								return true;
							}
							else if ((statusValue.equalsIgnoreCase("all") || statusValue.equalsIgnoreCase("unapproved")) && isUserAdmin) {
								return true;
							}
							else {
								response.sendError(HttpStatus.FORBIDDEN.value());
								return false;
							}
						}
						else {
							response.sendError(HttpStatus.FORBIDDEN.value());
							return false;
						}
					}
					else {
						response.sendError(HttpStatus.FORBIDDEN.value());
						return false;
					}
				}
				else if (uriParts.length == 7) {
					if (
						uriParts[3].equalsIgnoreCase("questions") && uriParts[5].equalsIgnoreCase("answers") &&
						(HttpMethod.PUT.matches(currentMethod) || HttpMethod.DELETE.matches(currentMethod)) &&
						isUserAdmin
					) {
						return true;
					}
					else {
						response.sendError(HttpStatus.FORBIDDEN.value());
						return false;
					}
				}
				else {
					response.sendError(HttpStatus.FORBIDDEN.value());
					return false;
				}
			}
			else {
				response.sendError(HttpStatus.FORBIDDEN.value());
				return false;
			}
		}
		response.sendError(HttpStatus.FORBIDDEN.value());
		return false;
    }
	
}
