package com.ssafy.gumid101.crew.manager;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.crew.CrewGoalType;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.entity.QCrewEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CrewManagerCustomRepositoryImpl implements CrewManagerCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<CrewDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto) {

		QCrewEntity crewEntity = new QCrewEntity("crew");

		BooleanBuilder builder = RecruitmentParamsProcessedCondition(crewEntity, paramsDto);

		jpaQueryFactory.from(crewEntity).where(builder).where(crewEntity.crewDateStart.after(LocalDateTime.now())) 
				.orderBy(crewEntity.crewDateStart.asc());
		// 시작일
		// 지난
		// 크루는
		// 보여주지
		// 않는다.
		return null;
	}

	private BooleanBuilder RecruitmentParamsProcessedCondition(QCrewEntity crewEntity, RecruitmentParamsDto paramsDto) {
		BooleanBuilder builder = new BooleanBuilder();

		if (StringUtils.hasLength(paramsDto.getTitle())) {
			// 쿼리 파라메터에 타이틀이 null이 아니고 lenght가 0이 아니라면
			builder.and(crewEntity.crewName.contains(paramsDto.getTitle()));
			// 크루의 이름은 해당 문자열을 가지고 있어야한다.
		}

		LocalDateTime startDay = null;
		LocalDateTime endDay = null;

		try {
			startDay = LocalDateTime.parse(paramsDto.getStartDay());

		} catch (Exception e) {

			log.warn("시작 시작범위 파싱 중 에러 : {}", e.getMessage());
		}

		try {
			endDay = LocalDateTime.parse(paramsDto.getEndDay());
		} catch (Exception e) {
			log.warn("시작 종료범위 파싱 중 에러 : {}", e.getMessage());
		}

		if (startDay != null) {
			// 시작 범위 지정이 있으면, 크루 시작일이 그 이후여야한다.
			builder.and(crewEntity.crewDateStart.after(startDay));
		}

		if (endDay != null) {
			// 시작일 끝 범위 지정이 있으면 크루 시작일은 끝 범위 지정 이전이여야한다.
			builder.and(crewEntity.crewDateStart.before(endDay));
		}

		if (paramsDto.getPointMin() != null) {
			// 포인트 최소가 있으면
			builder.and(crewEntity.crewCost.goe(paramsDto.getPointMin()));
		}
		if (paramsDto.getPointMax() != null) {
			builder.and(crewEntity.crewCost.loe(paramsDto.getPointMax()));
		}

		
		// 크루의 목표 타입 검증, 목표 타입의 범위 검색
		if (StringUtils.hasLength(paramsDto.getPurposeType())) {
			boolean isAccepted = false;
			if (CrewGoalType.DISTANCE.getKey().equals(paramsDto.getPurposeType())) {
				builder.and(crewEntity.crewGoalType.eq(CrewGoalType.DISTANCE.getKey()));
				isAccepted = true;
			}

			else if (CrewGoalType.TIME.getKey().equals(paramsDto.getPurposeType())) {
				builder.and(crewEntity.crewGoalType.eq(CrewGoalType.TIME.getKey()));
				isAccepted = true;
			}

			if(isAccepted) {
				if (paramsDto.getPurposeMinValue() != null) {

					builder.and(crewEntity.crewGoalAmount.goe(paramsDto.getPurposeMinValue()));
				}
				
				if (paramsDto.getPurposeMaxValue() != null) {
					builder.and(crewEntity.crewGoalAmount.loe(paramsDto.getPurposeMaxValue()));
				}
			}
			// 크루 목표의 거리와 시간..... 22시 ~ 01시는 아직 안 고려
			
		}
		
		//주에 몇번 성공해야하는지
		if(paramsDto.getGoalMinDay() != null) {
			builder.and(crewEntity.crewGoalDays.goe(paramsDto.getGoalMinDay() ));
		}
		
		if(paramsDto.getGoalMaxDay() != null) {
			builder.and(crewEntity.crewGoalDays.loe(paramsDto.getGoalMaxDay()));
		}

		return builder;
	}

}
