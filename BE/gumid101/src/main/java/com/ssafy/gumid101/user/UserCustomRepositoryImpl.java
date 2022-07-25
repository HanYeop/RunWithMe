package com.ssafy.gumid101.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.QCrewBoardEntity;
import com.ssafy.gumid101.entity.QCrewTotalRecordEntity;
import com.ssafy.gumid101.entity.QUserEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.res.RankingDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository{

	private final JPAQueryFactory jpqQueryFactory;
	
	/**
	 * @param user 특정 유저
	 * @param size 가져올 총 갯수
	 * @param offset 오프셋
	 */
	@Override
	public List<CrewBoardEntity> findUserBoardsWithOffestAndSize(UserEntity user, Long size, Long offset) {
		
		BooleanBuilder builder = new BooleanBuilder();
		
		
		QCrewBoardEntity crewBoard = new QCrewBoardEntity("cb");
		
		//크루 보드를 가져오는데 , 특정유저의 것만 , 날짜 오름차순으로
		JPAQuery<CrewBoardEntity> jpaQuery = jpqQueryFactory.selectFrom(crewBoard)
		.where(crewBoard.userEntity.eq(user))
		.orderBy(crewBoard.crewBoardRegTime.desc());
		
		
		if(offset ==null) {
			offset = 0L;
		}
		jpaQuery = jpaQuery.offset(offset);
		if(size!= null) {
			jpaQuery = jpaQuery.limit(offset+size);
		}
		
		List<CrewBoardEntity> crews =  jpaQuery.fetch();
		
		
				
		
		return crews;
	}
	

	@Override
	public List<RankingDto> getUserTotalPointRanking(Long size, Long offset){
		
		if(size == null) {
			size = (long)Integer.MAX_VALUE;
		}
		
		if(offset == null) {
			offset = 0L;
		}
		
		
		
		QUserEntity user = QUserEntity.userEntity;
		NumberExpression<?> column =  user.point.sum();
		List<RankingDto> rankingList = jpqQueryFactory.from(user)
		.select(Projections.fields(RankingDto.class, 
				user.userSeq.as("userSeq"),
				user.nickName.as("nickName"),
				column.as("lankingValue")))
		.orderBy(column.desc())
		.fetch();
		
		return rankingList;
	}
	
	@Override
	public CrewTotalRecordDto getMyTotalRecord(UserEntity userEntity) throws Exception{
		QCrewTotalRecordEntity qCrewTotalRecordEntity = QCrewTotalRecordEntity.crewTotalRecordEntity;
		BooleanBuilder builder = new BooleanBuilder();
		
		return jpqQueryFactory
				.select(
						Projections.fields(CrewTotalRecordDto.class, 
						qCrewTotalRecordEntity.totalCalorie.sum().as("totalCalorie"),
						qCrewTotalRecordEntity.totalDistance.sum().as("totalDistance"),
						qCrewTotalRecordEntity.totalTime.sum().as("totalTime"),
						qCrewTotalRecordEntity.totalLongestDistance.max().as("totalLongestDistance"),
						qCrewTotalRecordEntity.totalLongestTime.max().as("totalLongestTime")
					)
				)
				.from(qCrewTotalRecordEntity)
				.where(builder)
				.fetchOne();
	}

}
