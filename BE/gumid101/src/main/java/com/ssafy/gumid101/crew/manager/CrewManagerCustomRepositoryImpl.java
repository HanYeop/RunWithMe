package com.ssafy.gumid101.crew.manager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.crew.CrewGoalType;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.CrewSortType;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.QCrewEntity;
import com.ssafy.gumid101.entity.QUserCrewJoinEntity;
import com.ssafy.gumid101.res.CrewFileDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CrewManagerCustomRepositoryImpl implements CrewManagerCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * 모집 중인 크루 조회 and 검색 파라메터 값에 따라 조회 혹은 검색
	 */

	@Override
	public List<CrewFileDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto) {

		QCrewEntity crewEntity = new QCrewEntity("crew");

		BooleanBuilder builder = RecruitmentParamsProcessedCondition(crewEntity, paramsDto);

		Long maxCrewSeq = (paramsDto.getMaxCrewSeq() == null || paramsDto.getMaxCrewSeq() == 0) ? Long.MAX_VALUE
				: paramsDto.getMaxCrewSeq();
		// maxCrewSeq가 0이라는 것은 초기 검색임으로 정렬 최상위 부터 size만큼 반환
		// maxCrewwSeq가 0이 아니라는 것은, 스크롤 이후로 발생하는 것 , 따라서 maxCrewSeq가 값이 의미가 있이 오는데
		// 거기서 기존 검색 꺼는 주면 안되기 때문에 maxCrewSeq 밑으로 부터 반환해야한다.
		Long size = (paramsDto.getSize() == null || paramsDto.getSize() == 0) ? Long.MAX_VALUE : paramsDto.getSize();
		// size가 0이면 전체 검색하는 걸로 하자

		// 검색 조건에 따라 + 시작 안한 크루만 + 페이징

		OrderSpecifier<LocalDateTime> order1 = crewEntity.crewRegTime.desc(); // 기본정렬
		OrderSpecifier<Long> order2 = crewEntity.crewSeq.desc();
		// 2022-07-28 정렬 기능생김
		if (paramsDto.getSortType() == CrewSortType.REG_RECENT) {
			// 이 조건문 필요없는데, 그래도 이게 더 표현상 좋지 않을까?
			order1 = crewEntity.crewRegTime.desc();
		} else if (paramsDto.getSortType() == CrewSortType.STARTDATE_RECENT) {
			// 시작일 얼마 안남은 순
			order1 = crewEntity.crewDateStart.asc();
		}

		JPAQuery<CrewEntity> test = jpaQueryFactory.selectFrom(crewEntity).innerJoin(crewEntity.managerEntity)
				.where(builder).where(crewEntity.crewDateStart.after(LocalDateTime.now()))
				.where(crewEntity.crewSeq.lt(maxCrewSeq)).orderBy(order1, order2).limit(size);
		List<CrewEntity> crews = test.fetch();

		// 검색때는 마감 얼마 안 남은거 , 뿌릴때는 등록된 순서
		// orderby 바뀔지 안바뀔지 모르겟다. 현재는 크루 시작일이 얼마 남지 않은 순으로 반환한다.

		return crews.stream().map(

				(crew) -> {
					CrewDto crewDto = CrewDto.of(crew, crew.getManagerEntity().getNickName(),
							crew.getManagerEntity().getUserSeq());

					crewDto.setCrewMemberCount(crew.getUserCrewJoinEntitys().size());

					Optional<ImageFileEntity> ims = Optional.ofNullable(crew.getImageFile());

					ImageFileDto imgDto = null;

					if (ims.isPresent()) {
						imgDto = ImageFileDto.of(ims.get());
					} else {
						imgDto = ImageFileDto.getNotExist();
					}

					return new CrewFileDto(crewDto, imgDto);

				}

		).collect(Collectors.toList());

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

		if (paramsDto.getStartDay() != null) {
			try {
				startDay = LocalDateTime.parse(paramsDto.getStartDay()).withHour(0).withMinute(0).withSecond(0);

			} catch (Exception e) {

				log.warn("시작 시작범위 파싱 중 에러 : {}", e.getMessage());
			}
		}

		if (paramsDto.getEndDay() != null) {
			try {
				endDay = LocalDateTime.parse(paramsDto.getEndDay()).withHour(23).withMinute(59).withSecond(59);
			} catch (Exception e) {
				log.warn("시작 종료범위 파싱 중 에러 : {}", e.getMessage());
			}
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

			if (isAccepted) {
				if (paramsDto.getPurposeMinValue() != null) {

					builder.and(crewEntity.crewGoalAmount.goe(paramsDto.getPurposeMinValue()));
				}

				if (paramsDto.getPurposeMaxValue() != null) {
					builder.and(crewEntity.crewGoalAmount.loe(paramsDto.getPurposeMaxValue()));
				}
			}
			// 크루 목표의 거리와 시간..... 22시 ~ 01시는 아직 안 고려

		}

		// 주에 몇번 성공해야하는지
		if (paramsDto.getGoalMinDay() != null) {
			builder.and(crewEntity.crewGoalDays.goe(paramsDto.getGoalMinDay()));
		}

		if (paramsDto.getGoalMaxDay() != null) {
			builder.and(crewEntity.crewGoalDays.loe(paramsDto.getGoalMaxDay()));
		}

		return builder;
	}
	
	


}
