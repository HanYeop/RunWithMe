package com.ssafy.gumid101.achievement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.gumid101.entity.AchievementEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface AchievementRepository extends JpaRepository<AchievementEntity, Long>{

	@Query("SELECT a FROM AchievementEntity a WHERE a NOT IN (SELECT ac.achieveEntity FROM UserEntity u Join u.achievementCompleteEntitys ac) ")
	List<AchievementEntity> findNotAchivement(UserEntity userEntity);

}
