package com.ssafy.gumid101.crew.activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.entity.RunRecordEntity;

public interface CrewActivityRunDslRepository{

	List<RunRecordEntity> getRunRecords(RecordParamsDto condition);

}
