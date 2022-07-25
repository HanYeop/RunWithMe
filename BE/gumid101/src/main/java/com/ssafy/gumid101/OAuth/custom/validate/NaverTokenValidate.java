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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NaverTokenValidate implements CustomTokenValidator {

	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String CLIENT_ID;


	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String CLIENT_SECRET;
	
	@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
	private String USER_INFO_URL;
	@Override
	public Map<String, Object> validate(String idTokenString) throws GeneralSecurityException, IOException {
		// TODO Auto-generated method stub
		/*
	
	RestTemplate rt = new RestTemplate();
		HttpHeaders accessTokenHeaders = new HttpHeaders();
		accessTokenHeaders.add("Content-type", "application/x-www-form-urlencoded");

		MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
		accessTokenParams.add("grant_type", "authorization_code");
		accessTokenParams.add("client_id", "{client_id");
		accessTokenParams.add("client_secret", "{client_secret}");
		accessTokenParams.add("code", idTokenString); // 응답으로 받은 코드

		HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams,
				accessTokenHeaders);

		ResponseEntity<String> accessTokenResponse = rt.exchange("https://nid.naver.com/oauth2.0/token",
				HttpMethod.POST, accessTokenRequest, String.class);

		Map<String, Object> map = new HashMap<String, Object>();
*/
		
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
		
		return  map;
	}

}
