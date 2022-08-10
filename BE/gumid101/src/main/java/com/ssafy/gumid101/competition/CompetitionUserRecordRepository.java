package com.ssafy.gumid101.competition;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CompetitionEntity;
import com.ssafy.gumid101.entity.CompetitionUserRecordEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface CompetitionUserRecordRepository extends JpaRepository<CompetitionUserRecordEntity, Long>{
	Optional<CompetitionUserRecordEntity> findByUserEntityAndCompetitionEntity(UserEntity userEntity, CompetitionEntity competitionEntity);
	List<CompetitionUserRecordEntity> findByUserEntity_userSeq(Long userSeq);
	List<CompetitionUserRecordEntity> findByCompetitionEntityOrderByCompetitionDistanceDesc(CompetitionEntity competitionEntity);

}
