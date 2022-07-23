package com.ssafy.gumid101.crew.activity;

import java.util.List;

import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;

public interface CrewActivityService {
	List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto);
}
