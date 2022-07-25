package com.ssafy.gumid101.crew.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	private final CrewActivityRunDslRepository crewRunRepo;
	private final CrewActivityBoardRepository boardRepo;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	@Override
	public List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto) {

		List<RunRecordDto> runRecordList = crewRunRepo.getRunRecords(recordParamsDto).stream().map((entity) -> {
			return RunRecordDto.of(entity);
		}).collect(Collectors.toList());
		
		return runRecordList;
	}
	
	@Override
	public List<RunRecordDto> getMyRecordList(RecordParamsDto recordParamsDto) {
		recordParamsDto.setUserSeq(loadUserFromToken().getUserSeq());
		return getCrewRecordList(recordParamsDto);
	}
	

	@Override
	public CrewBoardDto writeBoard(MultipartFile image, UserDto tokenUser, CrewBoardDto content) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
