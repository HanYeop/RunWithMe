package com.ssafy.gumid101.crew;

import java.util.List;

import com.ssafy.gumid101.dto.CoordinateDto;
import com.ssafy.gumid101.dto.RecordCoordinateDto;

public interface RunRecordCustomRepository {
	int[] coordinatesInsertBatch(Long recordSeq, List<CoordinateDto> coordinates);

	List<RecordCoordinateDto> getCoordinateByRunRecordSeq(Long recordSeq);
}
