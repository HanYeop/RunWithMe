package com.ssafy.gumid101.firebase;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class FirebaseMessageImpl implements FirebaseMessage{
	private final ObjectMapper objectMapper;
	
	/**
	 * 
	 * @param targetToken 보낼 토큰
	 * @param title       제목
	 * @param body        내용
	 * @throws IOException
	 */
	// fire_base:
	// api_url:
	// "https://fcm.googleapis.com/v1/projects/runwithme-357006/messages:send"
	// firebase_config_path: "firebase/google_services.json"
	// scoped: "https://www.googleapis.com/auth/cloud-platform"

	@Value("${fire_base.api_url}")
	private String API_URL;

	@Value("${fire_base.firebase_config_path}")
	private String FIREBASE_CONFIG_PATH;

	@Value("${fire_base.scoped}")
	private String SCOPE_PATH;

	public void sendMessageTo(List<FcmMessage.Message> fcmMesageList) throws IOException {
		
		for(FcmMessage.Message message : fcmMesageList) {
			sendMessageTo(message.getToken(), message.getNotification());
		}
		
	}
	public void sendMessageTo(String targetToken,FcmMessage.Notification notification) throws IOException {
		sendMessageTo(targetToken,notification.getTitle(),notification.getBody());
	}
	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
		String message = makeMessage(targetToken, title, body);

		URL url = new URL(API_URL);

		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
		conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
		conn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, Integer.toString(bytes.length));
		conn.setDoOutput(true);
		
		
		try (OutputStream outStream = conn.getOutputStream()) {

			outStream.write(bytes);
			outStream.flush();

		} catch (Exception e) {
			log.warn(e.getMessage());
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

			String buffer = null;
			log.info("FCM이 응답한 메세지를 출력합니다.");
			while ((buffer = br.readLine()) != null) {
				log.info(buffer);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		conn.disconnect();
		// System.out.println(response.body().string());
	}

	private String makeMessage(String targetToken, String title, String body)
			throws JsonParseException, JsonProcessingException {

		if(targetToken == null) {
			targetToken = "";
		}
		if(title == null) {
			title = "";
		}
		if(body == null) {
			body = null;
		}
		FcmMessage fcmMessage = FcmMessage.builder()
				.message(FcmMessage.Message.builder().token(targetToken)
						.notification(FcmMessage.Notification.builder().title(title).body(body).image("").build())
						.build())
				.validateOnly(true).build();

		return objectMapper.writeValueAsString(fcmMessage);
	}

	private String getAccessToken() throws IOException {

		ArrayList<String> arr = new ArrayList<String>();
		arr.add(SCOPE_PATH);
		
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream()).createScoped(arr);

		googleCredentials.refreshIfExpired();
		// return googleCredentials.getAccessToken().getTokenValue();
		return googleCredentials.getAccessToken().getTokenValue();
	}
}
