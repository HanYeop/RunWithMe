package com.ssafy.gumid101.crew;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.RunRecordEntity;

public interface RunRecordRepository extends JpaRepository<RunRecordEntity, Long>  {

	List<RunRecordEntity> findAllByUserEntity_userSeqAndCrewEntity_crewSeqAndRunRecordStartTimeBetween(Long userSeq,
			Long crewSeq, LocalDateTime start, LocalDateTime end);
}
