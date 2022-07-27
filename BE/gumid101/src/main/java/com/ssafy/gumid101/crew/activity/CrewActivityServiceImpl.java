package com.ssafy.gumid101.crew.activity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrewActivityServiceImpl implements CrewActivityService{
	
	private final CrewActivityRunDslRepository crewRunRepo;
	private final CrewActivityBoardRepository boardRepo;
	private final UserCrewJoinRepository ucRepo;
	private final RunRecordRepository runRepo;

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
	public Boolean isMyTodayRecord(Long userSeq, Long crewSeq) throws Exception {
		// 해당 크루가 자기 크루인지 1차 확인
		UserCrewJoinEntity userCrewJoinEntity = ucRepo.findByUserEntity_UserSeqAndCrewEntity_CrewSeq(userSeq, crewSeq)
				.orElseThrow(() -> new NotFoundUserException("자신의 소속 크루의 활동만 조회할 수 있습니다."));
		LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
		List<RunRecordEntity> myToday = null;
		try{
			 myToday = runRepo.findAllByUserEntity_userSeqAndCrewEntity_crewSeqAndRunRecordStartTimeBetween(userSeq, crewSeq, start, end);
		} catch (Exception e) {
			throw new Exception("기록 조회에 실패했습니다.");
		}
		if (myToday != null && myToday.size() > 0){
			return true;
		}
		return false;
	}
	

	@Override
	public CrewBoardDto writeBoard(MultipartFile image, UserDto tokenUser, CrewBoardDto content) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
