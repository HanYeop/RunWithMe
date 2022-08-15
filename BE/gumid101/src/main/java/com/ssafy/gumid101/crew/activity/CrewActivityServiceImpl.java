package com.ssafy.gumid101.crew.activity;

import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.crew.UserCrewRunRecordRepository;
import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.CrewTotalRecordEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.res.GraphRecordDto;
import com.ssafy.gumid101.res.RankingDto;
import com.ssafy.gumid101.user.UserRepository;

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
	private final UserCrewRunRecordRepository totalRepo;
	private final UserRepository userRepo;
	private final CrewManagerRepository crewManageRepo;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	
	@Override
	public List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto) {
//2022-07-29 ### N+1 문제 발생
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
	public Boolean getRunabletoday(Long userSeq, Long crewSeq) throws Exception {
		// 해당 크루가 자기 크루인지 1차 확인
		UserCrewJoinEntity ucjEntity = ucRepo.findByUserEntity_UserSeqAndCrewEntity_CrewSeq(userSeq, crewSeq)
				.orElseThrow(() -> new NotFoundUserException("자신의 소속 크루의 활동만 조회할 수 있습니다."));

		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalTime nowTime = LocalTime.now();
		if (ucjEntity.getCrewEntity().getCrewDateStart().isAfter(nowDateTime) || ucjEntity.getCrewEntity().getCrewDateEnd().isBefore(nowDateTime)) {
			throw new CrewPermissonDeniedException("크루 활동 기간이 아닙니다.");
		}
		if (ucjEntity.getCrewEntity().getCrewTimeStart().isAfter(nowTime) || ucjEntity.getCrewEntity().getCrewTimeEnd().isBefore(nowTime)) {
			throw new CrewPermissonDeniedException("크루 활동 시간이 아닙니다.");
		}
		List<RunRecordEntity> myToday = null;
		try{
			 myToday = runRepo.findByUserEntityAndCrewEntityAndRunRecordStartTimeBetween(ucjEntity.getUserEntity(), ucjEntity.getCrewEntity(), nowDateTime.withHour(0).withMinute(0).withSecond(0), nowDateTime.withHour(23).withMinute(59).withSecond(59));
		} catch (Exception e) {
			throw new CrewPermissonDeniedException("기록 조회에 실패했습니다.");
		}
		if (myToday != null && myToday.size() > 0){
			throw new CrewPermissonDeniedException("오늘 이미 달린 기록이 존재합니다.");
		}
		return true;
	}
	

	/**
	 * 크루내에서 내 기록들을 반환
	 */
	@Override
	public List<RunRecordDto> getMyCrewRecordsByParam(RecordParamsDto recordParamsDto) throws Exception {
		List<RunRecordDto> myRunrecordInCrew = crewRunRepo.selectByCrewAndUserSeqWithOffsetSize(recordParamsDto);
		return myRunrecordInCrew;
	}
	
	@Override
	public List<GraphRecordDto> getCrewMyGraphData(UserDto userDto, Long crewSeq, String goalType) throws Exception {
		UserEntity userEntity = userRepo.findById(userDto.getUserSeq())
				.orElseThrow(() -> new NotFoundUserException("접속정보가 올바르지 않습니다."));
		CrewEntity crewEntity = crewManageRepo.findById(crewSeq)
				.orElseThrow(() -> new NotFoundUserException("크루 정보가 올바르지 않습니다."));
		LocalDateTime now = LocalDateTime.now();
		if (crewEntity.getCrewDateStart().isAfter(now)) {
			throw new NotFoundUserException("시작된 크루만 그래프 기록을 지원합니다.");
		}
		UserCrewJoinEntity ucjEntity = ucRepo.findByUserEntityAndCrewEntity(userEntity, crewEntity)
				.orElseThrow(() -> new NotFoundUserException("자신의 소속 크루의 활동만 조회할 수 있습니다."));

		
		List<RunRecordEntity> myCrewRecordList = runRepo.findByUserEntityAndCrewEntity(userEntity, crewEntity);
		// 데이터가 올바르다면 정렬 안 해도 되는걸로 안다. 그래도 혹시몰라 정렬
		myCrewRecordList.sort(new Comparator<RunRecordEntity>() {
			@Override
			public int compare(RunRecordEntity o1, RunRecordEntity o2) {
				if (o1.getRunRecordStartTime().isBefore(o2.getRunRecordStartTime())) {
					return -1;
				}
				else if (o1.getRunRecordStartTime().isAfter(o2.getRunRecordStartTime())) {
					return 1;
				}
				// 사실 같은 시점이 들어오면 에러다.
				return 0;
			}
		});
		List<GraphRecordDto> crewMyGraphDataList = new ArrayList<>();
		LocalDateTime timeSplit = crewEntity.getCrewDateStart().plusDays(1).minusSeconds(1);
		int idx = 0;
		while(idx < myCrewRecordList.size()) {
			// 해당 크루에서 자신이 뛴 기록이 하루에 최대 0가 또는 1개가 있다는 것이 보장될 때 사용가능한 논리.
			if (myCrewRecordList.get(idx).getRunRecordStartTime().isAfter(timeSplit)) {
				// 해당 일차 기록이 없을 경우
				crewMyGraphDataList.add(GraphRecordDto.builder()
						.amount(0)
						.month(timeSplit.getMonthValue())
						.day(timeSplit.getDayOfMonth())
						.build());
			}
			else {
				// 해당 칠차 기록이 있을 경우
				if (goalType.equals("time")) {
					// 목표가 time일 때
					crewMyGraphDataList.add(GraphRecordDto.builder()
							.amount(myCrewRecordList.get(idx).getRunRecordRunningTime())
							.month(timeSplit.getMonthValue())
							.day(timeSplit.getDayOfMonth())
							.build());
				}
				else {
					// 목표가 distance일 때
					crewMyGraphDataList.add(GraphRecordDto.builder()
							.amount(myCrewRecordList.get(idx).getRunRecordRunningDistance())
							.month(timeSplit.getMonthValue())
							.day(timeSplit.getDayOfMonth())
							.build());
				}
				idx++;
			}
			timeSplit = timeSplit.plusDays(1);
		}
		while(now.isAfter(timeSplit)) {
			// 현재 시간이 기준점보다 1초라도 더 지났으면 한 칸 더 반환해야함.
			if (timeSplit.isAfter(crewEntity.getCrewDateEnd())) {
				// 기준점이 종료시점 뒤라면 그만함.
				break;
			}
			crewMyGraphDataList.add(GraphRecordDto.builder()
					.amount(0)
					.month(timeSplit.getMonthValue())
					.day(timeSplit.getDayOfMonth())
					.build());
			timeSplit = timeSplit.plusDays(1);
		}
		if (crewMyGraphDataList.size() == 0 || crewMyGraphDataList.get(crewMyGraphDataList.size() - 1).getMonth() != now.getMonthValue() || crewMyGraphDataList.get(crewMyGraphDataList.size() - 1).getDay() != now.getDayOfMonth()) {
			crewMyGraphDataList.add(GraphRecordDto.builder()
					.amount(0)
					.month(now.getMonthValue())
					.day(now.getDayOfMonth())
					.build());
		}
		
		return crewMyGraphDataList;
	}

	/**
	 * 크루 내에서 사용자의 랭킹들을 반환
	 */
	@Override
	public List<RankingDto> getCrewRankingByParam(RankingParamsDto rankingParamsDto) throws Exception {
		

		
		List<RankingDto> rakingInCrewList = crewRunRepo.selectAllCrewRanking(rankingParamsDto);
		return rakingInCrewList;
		
	}
/**
 * 해당 크루 내 누적기록 반환
 */
	@Override
	public CrewTotalRecordDto getMyCrewTotalRecord(Long userSeq, Long crewSeq) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("해당하는 유저가 없습니다."));
		CrewEntity crewEntity = crewManageRepo.findById(crewSeq)
				.orElseThrow(() -> new NotFoundUserException("해당하는 크루가 없습니다."));
		CrewTotalRecordEntity myCrewTotal = totalRepo.findByUserEntityAndCrewEntity(userEntity, crewEntity).orElse(new CrewTotalRecordEntity());
		
		return CrewTotalRecordDto.of(myCrewTotal);
	}

}
