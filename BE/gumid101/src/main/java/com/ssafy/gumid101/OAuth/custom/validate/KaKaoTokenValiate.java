package com.ssafy.gumid101.OAuth.custom.validate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KaKaoTokenValiate implements CustomTokenValidator{
	
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String CLIENT_ID;


	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String CLIENT_SECRET;
	
	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String USER_INFO_URL;
	
	
	@Override
	public Map<String, Object> validate(String idTokenString) throws GeneralSecurityException, IOException {
		
		
		RestTemplate rt = new RestTemplate();
		
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    // json -> 객체로 매핑하기 위해 NaverOauthParams 클래스 생성

	    
	    // header를 생성해서 access token을 넣어줍니다.
	    HttpHeaders profileRequestHeader = new HttpHeaders();
	    profileRequestHeader.add("Authorization", "Bearer " +idTokenString);
	    
	    HttpEntity<HttpHeaders> profileHttpEntity = new HttpEntity<>(profileRequestHeader);
	    
	    // profile api로 생성해둔 헤더를 담아서 요청을 보냅니다.
	    ResponseEntity<String> profileResponse = rt.exchange(
	    		USER_INFO_URL,
	            HttpMethod.POST,
	            profileHttpEntity,
	            String.class
	    );
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kk", profileResponse.getBody());
		return map;
		
	}
	

}
