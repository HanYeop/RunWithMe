package com.ssafy.gumid101.crew;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface RunRecordRepository extends JpaRepository<RunRecordEntity, Long>  {
	List<RunRecordEntity> findByUserEntityAndCrewEntityAndRunRecordStartTimeBetween(UserEntity userEntity, CrewEntity crewEntity, LocalDateTime start, LocalDateTime end);
	List<RunRecordEntity> findByUserEntityAndCrewEntity(UserEntity userEntity, CrewEntity crewEntity);
	
	@Query("SELECT r FROM RunRecordEntity r WHERE r.userEntity.userSeq = :userSeq AND r.crewEntity.crewSeq = :crewSeq AND r.runRecordCompleteYN = 'Y' ORDER BY r.runRecordStartTime ASC,r.runRecordSeq ASC")
	List<RunRecordEntity> findByUserEntity_userSeqAndCrewEntity_crewSeq(@Param("userSeq")Long userSeq,@Param("crewSeq") Long crewSeq);
	
	Long countByUserEntityAndCrewEntityAndRunRecordStartTimeBetweenAndRunRecordCompleteYN(UserEntity userEntity, CrewEntity crewEntity, LocalDateTime weeksStart, LocalDateTime weeksEnd, String completeYN);
	Long countByUserEntity(UserEntity userEntity);
	List<RunRecordEntity> findByCrewEntityAndRunRecordCompleteYNOrderByRunRecordStartTime(CrewEntity crewEntity, String runRecordCompleteYN);
}
