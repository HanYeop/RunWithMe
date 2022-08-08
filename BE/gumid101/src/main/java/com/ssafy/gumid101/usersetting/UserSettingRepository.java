package com.ssafy.gumid101.usersetting;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.UserSettingEntity;

public interface UserSettingRepository extends JpaRepository<UserSettingEntity, Long>{
	Optional<UserSettingEntity> findByUserSeq(Long userSeq);
}
