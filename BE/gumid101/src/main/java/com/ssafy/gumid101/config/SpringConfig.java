package com.ssafy.gumid101.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.gumid101.interceptor.InterceptorControllerForLoggin;

@EnableAspectJAutoProxy
@Configuration
@EnableJpaAuditing
public class SpringConfig implements WebMvcConfigurer {

//	//ObjectMapper 는 쓰레드 세이프하다. 하위버전에서 락으로 인해 성능이슈가 있었으나 해결됨
//	@Bean 
//	public ObjectMapper objectMapper() {
//		return new ObjectMapper();
//	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new InterceptorControllerForLoggin()).addPathPatterns("/**");
	}

//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
//	}

}
