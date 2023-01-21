/*
* @Author: Devashish Ashok Pathrabe
* Modified Date: 26-08-2022
* Description: Authorization Configuration
*/

package com.wipro.cp.doconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wipro.cp.doconnect.interceptor.AuthorizationInterceptor;

@Configuration
@EnableWebMvc
public class AuthorizationConfig implements WebMvcConfigurer {
	
	@Bean
	public AuthorizationInterceptor authorizationInterceptor() {
	    return new AuthorizationInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**");
	}

}
