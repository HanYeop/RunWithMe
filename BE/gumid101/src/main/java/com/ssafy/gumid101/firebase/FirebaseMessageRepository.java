package com.ssafy.gumid101.firebase;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.FCMEntity;

public interface FirebaseMessageRepository extends JpaRepository<FCMEntity, Long>{

}
