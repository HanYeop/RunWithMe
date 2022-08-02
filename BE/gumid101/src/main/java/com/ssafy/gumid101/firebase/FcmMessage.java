package com.ssafy.gumid101.firebase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
	@JsonProperty("validate_only")
	private boolean validateOnly;
	private Message message;

	@Builder
	@AllArgsConstructor
	@Getter
	public static class Message {
		private Notification notification;
		private String token = "";
	}

	@Builder
	@AllArgsConstructor
	@Getter
	public static class Notification {
		private String title;
		private String body;
		private String image;
	}
	
	public static Message ofMessage(String token,String title,String content) {
		
		Notification notification = Notification.builder().title(title).body(content).image(null).build();
		
		
		
		return Message.builder().token(token).notification(notification).build();
	}
}
