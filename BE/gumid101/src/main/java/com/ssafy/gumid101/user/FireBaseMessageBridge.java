package com.ssafy.gumid101.user;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ssafy.gumid101.customexception.FCMTokenUnValidException;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.firebase.FirebaseMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
class FireBaseMessageBridge {
	
	private final FirebaseMessage firebaseMessage;
	private final UserRepository userRepo;
	/**
	 * 특정 유저 SEQ를 통하여 해당 유저에게 제목,메세지를 전달한다. 
	 */
	@Transactional
	public int sendMessageByFCM(Long userSeq,String title,String content) {
		Optional<UserEntity> userEntity = userRepo.findById(userSeq);
		if(!userEntity.isPresent()) {
			return 0;
		}
		return sendMessageByFCM(userEntity.get(),title,content);
	}
	@Transactional
	public int sendMessageByFCM(UserEntity userEntity,String title,String contetent) {
		
		if(userEntity == null) {
			return 0;
		}
		
		String targetToken = userEntity.getFcmToken();
		if(targetToken == null) {
			return 0;
		}
		
		try {
			firebaseMessage.sendMessageTo(targetToken, title, contetent);
		} catch ( FCMTokenUnValidException e) {
			
			log.debug("전송 실패 , 서버 응답 : 404 | 400 -reason : {}",e.getMessage());
			log.debug(e.getErrorResponse());
			log.debug("User_SEQ : {} 의  토큰 필드를 초기화 합니다.",userEntity.getUserSeq());
			userEntity.setFcmToken(null); //여기 왔다는 것은 그 토큰은 애초에 못쓴다는 것이다.
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return 1;
	}
	
}