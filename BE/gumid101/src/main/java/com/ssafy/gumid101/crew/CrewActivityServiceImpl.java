package com.ssafy.gumid101.crew;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.entity.RunRecordEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrewActivityServiceImpl implements CrewActivityService{
	
	@Autowired
	private final CrewActivityRunRepository crewRunRepo;
	
	@Override
	public List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto) {
		
		return crewRunRepo.getRunRecords(recordParamsDto);
	}

}
