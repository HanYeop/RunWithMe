package com.ssafy.gumid101.competition;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CompetitionEntity;
import com.ssafy.gumid101.entity.CompetitionUserEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface CompetitionUserRepository extends JpaRepository<CompetitionUserEntity, Long>{
	List<CompetitionUserEntity> findByUserEntity_userSeq(Long userSeq);
	Optional<CompetitionUserEntity> findByUserEntityAndCompetitionEntity(UserEntity userEntity, CompetitionEntity competitionEntity);

}
