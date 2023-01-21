/*
* @Author: Devashish Ashok Pathrabe
* Modified Date: 26-08-2022
* Description: Simple CORS Filter Component
*/

package com.wipro.cp.doconnect.component;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
	    HttpServletResponse httpResponse = (HttpServletResponse) response;
	    httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
	    httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
	    httpResponse.setHeader("Access-Control-Max-Age", "3600");
	    httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, remember-me, Authorization");
	    httpResponse.setHeader("Access-Control-Expose-Headers", "Location");
	    if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
	    	httpResponse.setStatus(HttpServletResponse.SC_OK);
	    } else {
        	chain.doFilter(request, response);
	    }
	}
	
	@Override
	public void init(FilterConfig filterConfig) {}
	
	@Override
	public void destroy() {}

}
