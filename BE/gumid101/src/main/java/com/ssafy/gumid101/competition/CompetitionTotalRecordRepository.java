package com.ssafy.gumid101.competition;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CompetitionEntity;
import com.ssafy.gumid101.entity.CompetitionTotalRecordEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface CompetitionTotalRecordRepository extends JpaRepository<CompetitionTotalRecordEntity, Long>{
	Optional<CompetitionTotalRecordEntity> findByUserEntityAndCompetitionEntity(UserEntity userEntity, CompetitionEntity competitionEntity);

}
