package com.ssafy.gumid101.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterceptorControllerForLoggin implements HandlerInterceptor {
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String ip = request.getHeader("X-Forwarded-For") ;
		
		if(ip == null) {
			ip = request.getRemoteAddr();
		}
		
		log.debug("[인터셉터 - 컨트롤러] 클라이언트 : {} - 요청 url :({}) {}", ip,request.getMethod() ,request.getRequestURL()); 
		
		
		return true; //로깅용 이니깐 무조건
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		log.debug("[인터셉터 - 컨트롤러]"); 
	}
}
