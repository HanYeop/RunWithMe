package com.ssafy.gumid101.firebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class FirebaseServiceImpl {
	private final ObjectMapper objectMapper;
	/**
	 * 
	 * @param targetToken 보낼 토큰
	 * @param title       제목
	 * @param body        내용
	 * @throws IOException
	 */

	private String API_URL;
	private String FIREBASE_CONFIG_PATH;
	private String SCOPE_PATH;

	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        URL url = new URL(API_URL);
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer "+getAccessToken());
        conn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, Integer.toString( message.length()));
        
        byte[] bytes = message.getBytes();
        
       try (OutputStream outStream  = conn.getOutputStream()){

           outStream.write(bytes);
           outStream.flush();
          
       }catch (Exception e){
    	   log.warn(e.getMessage());
       }
       
       try (BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream()))){
    	   
    	   String buffer = null;
    	   log.info("FCM이 응답한 메세지를 출력합니다.");
    	   while((buffer = br.readLine()) != null) {
    		   log.info(buffer);
    	   }
    	   
       }
        
        
        
        
        conn.disconnect();
        
      //  System.out.println(response.body().string());
    }

	private String makeMessage(String targetToken, String title, String body)
			throws JsonParseException, JsonProcessingException {

		FcmMessage fcmMessage = FcmMessage.builder()
				.message(FcmMessage.Message.builder().token(targetToken)
						.notification(FcmMessage.Notification.builder().title(title).body(body).image(null).build())
						.build())
				.validateOnly(false).build();

		return objectMapper.writeValueAsString(fcmMessage);
	}

	private String getAccessToken() throws IOException {

		ArrayList<String> arr = new ArrayList<String>();

		arr.add(SCOPE_PATH);
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream()).createScoped(arr);

		// googleCredentials.refreshIfExpired();
		// return googleCredentials.getAccessToken().getTokenValue();
		return googleCredentials.getAccessToken().getTokenValue();
	}
}
