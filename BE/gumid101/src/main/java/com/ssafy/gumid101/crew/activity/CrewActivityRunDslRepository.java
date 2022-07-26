package com.ssafy.gumid101.crew.activity;

import java.time.LocalDateTime;
import java.util.List;

import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.entity.RunRecordEntity;

public interface CrewActivityRunDslRepository{

	List<RunRecordEntity> getRunRecords(RecordParamsDto condition);

	List<RunRecordEntity> findAllByUserEntity_userSeqAndCrewEntity_crewSeqAndRunRecordStartTimeBetween(Long userSeq,
			Long crewSeq, LocalDateTime start, LocalDateTime end);

}
