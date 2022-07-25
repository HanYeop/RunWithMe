package com.ssafy.gumid101.achievement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.AchievementEntity;

public interface AchievementRepository extends JpaRepository<AchievementEntity, Long>{

}
