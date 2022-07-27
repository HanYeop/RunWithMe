package com.ssafy.gumid101.totalranking;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.entity.QCrewTotalRecordEntity;
import com.ssafy.gumid101.res.RankingDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CrewTotalRecordCustomRepositoryImpl implements CrewTotalRecordCustomRepository{

	private final JPAQueryFactory jpaQueryFactory;

	public List<RankingDto> getUserTotalRankingOptionType(String type, Long size, Long offset) {
		
		QCrewTotalRecordEntity crewTotal = new QCrewTotalRecordEntity("t_ctr");

		// user crew 키를 identity로 각 통합랭킹을 관리하는 테이블에서 user의 통합랭킹을 얻기 위하여
		// user로 그룹핑을 실행
		NumberExpression<?> typeField = null;

		if ("distance".equals(type)) {
			typeField = crewTotal.totalDistance.sum();
		} else if ("time".equals(type)) {
			typeField = crewTotal.totalTime.sum();
		}
		
		if(size == null) {
			size = (long)Integer.MAX_VALUE;
		}
		
		if(offset == null) {
			offset = 0L;
		}

		List<RankingDto> rankings = jpaQueryFactory.from(crewTotal)
				.select(Projections.fields(RankingDto.class, crewTotal.userEntity.nickName.as("userName"),
						crewTotal.userEntity.userSeq.as("userSeq"), typeField.as("lankingValue")))
				.groupBy(crewTotal.userEntity.userSeq)
				.orderBy(typeField.desc())
				.offset(offset).limit(offset + size).fetch();

		return rankings;
	}

}
