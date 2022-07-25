package com.ssafy.gumid101.crew.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrewActivityServiceImpl implements CrewActivityService{
	
	private final CrewActivityRunRepositoryDSL crewRunRepo;
	private final CrewActivityBoardRepository boardRepo;
	
	@Override
	public List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto) {

		List<RunRecordDto> runRecordList = crewRunRepo.getRunRecords(recordParamsDto).stream().map((entity) -> {
			return RunRecordDto.of(entity);
		}).collect(Collectors.toList());
		
		return runRecordList;
		
	}

	@Override
	public CrewBoardDto writeBoard(MultipartFile image, UserDto tokenUser, CrewBoardDto content) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
