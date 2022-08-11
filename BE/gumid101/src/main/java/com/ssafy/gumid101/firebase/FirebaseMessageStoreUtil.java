package com.ssafy.gumid101.firebase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssafy.gumid101.entity.FCMEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FirebaseMessageStoreUtil {

	private final FirebaseMessageRepository fcmRepo;

	public int storeFcmMessage(List<FCMEntity> messages) {

		fcmRepo.saveAllAndFlush(messages);

		return messages.size();
	}

	public int storeFcmMessage(String token, String title, String content) {
		return storeFcmMessage(token, title, content, null);
	}

	public int storeFcmMessage(String token, String title, String content, Long userSeq) {

		FCMEntity fcmEntity = FCMEntity.of(token, title, content, userSeq);
		fcmRepo.save(fcmEntity);
		return 1;
	}

	
	public void deleteFcmMessage(FCMEntity fcmEntity) {
		fcmRepo.delete(fcmEntity);
	}
	public void deleteFcmMessage(List< FCMEntity> fcmEntities) {
		fcmRepo.deleteAllInBatch(fcmEntities);
	}
	
}
