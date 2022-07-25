package com.ssafy.gumid101.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.res.RankingDto;

public interface UserRepository extends JpaRepository<UserEntity, Long>,UserCustomRepository{
	Optional<UserEntity> findByEmail(String email); // 이미 email을 통해 생성된 사용자인지 체크

	int countByEmail(@Param("email") String email);

	int countByNickName(@Param("nickName") String nickname);


}
