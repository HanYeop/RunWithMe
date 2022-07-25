package com.ssafy.gumid101.achievement;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.entity.AchievementCompleteEntity;

public interface AchievementCompleteRepository extends JpaRepository<AchievementCompleteEntity, Long>{

	@Query("select ac from AchievementCompleteEntity ac where ac.userEntity.userSeq = :userSeq")
	Collection<AchievementCompleteEntity> findByUserSeq(Long userSeq);

}
