package com.ssafy.gumid101.user;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import com.ssafy.gumid101.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {
	Optional<UserEntity> findByEmail(String email); // 이미 email을 통해 생성된 사용자인지 체크

	int countByEmail(@Param("email") String email);

	Optional<UserEntity> findByNickNameAndUserState(@Param("nickName") String nickname, @Param("user_state") String delete_yn);

	int countByNickName(@Param("nickName") String nickname);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<UserEntity> findWithLockingByUserSeq(Long userSeq);

}
