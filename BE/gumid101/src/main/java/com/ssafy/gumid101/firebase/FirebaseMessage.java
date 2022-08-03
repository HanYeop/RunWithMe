package com.ssafy.gumid101.firebase;

import java.io.IOException;
import java.util.List;

import com.ssafy.gumid101.customexception.FCMTokenUnValidException;

public interface FirebaseMessage {
	public void sendMessageTo(String targetToken, String title, String body) throws IOException, FCMTokenUnValidException ;

	
	public void sendMessageTo(List<FcmMessage.Message> fcmMesageList) throws IOException ;
	
	public void sendMessageTo(String targetToken,FcmMessage.Notification notification) throws IOException, FCMTokenUnValidException;
}
