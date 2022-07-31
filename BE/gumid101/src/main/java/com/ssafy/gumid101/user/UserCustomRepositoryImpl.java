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
import com.ssafy.gumid101.entity.QCrewEntity;
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
	public List<CrewBoardEntity> findUserBoardsWithOffestAndSize(UserEntity user, Long size, Long maxBoardSeq) {
		
		BooleanBuilder builder = new BooleanBuilder();
		
		
		QCrewBoardEntity crewBoard = new QCrewBoardEntity("cb");
		QCrewEntity crew = new QCrewEntity("c");
		
		if(maxBoardSeq != null && maxBoardSeq != 0  ) {
			builder.and(crewBoard.crewBoardSeq.lt(maxBoardSeq));
		}
		if(size == null || size==0 ) {
			size = Long.MAX_VALUE;
		}
		//크루 보드를 가져오는데 , 특정유저의 것만 , 날짜 오름차순으로
		//크루 보드를 탐색하면서 주어진 유저랑 같은 거만 추스린 후에
		//나온 보드의 크루를 구한다.
		//근데 maxBoardSeq 가 0이 아니면 maxBoardSeq 이하인 거에서만 구한다. 페이징을 위하여
		//최근 작성일 순으로
		JPAQuery<CrewBoardEntity> jpaQuery = jpqQueryFactory.selectFrom(crewBoard)
				.innerJoin(crewBoard.userEntity)
				.on(crewBoard.userEntity.eq(user))
				.innerJoin(crewBoard.crewEntity,crew).fetchJoin()
		.where(builder)
		.limit(size)
		.orderBy(crewBoard.crewBoardRegTime.desc(),crewBoard.crewBoardSeq.desc());
		//자신의 글만 최신 순으로 가져온다. 근데 ... 
		//자신이 쓴 글이 어디 크루거인지 알아야함

		
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
		NumberExpression<?> column =  user.point;
		List<RankingDto> rankingList = jpqQueryFactory.from(user)
		.select(Projections.fields(RankingDto.class, 
				user.userSeq.as("userSeq"),
				user.nickName.as("userName"),
				column.as("rankingValue"),user.imageFile.imgSeq.coalesce(0L).as("imgSeq")))
		.orderBy(column.desc()).offset(offset).limit(offset+size)
		.fetch();
		
		for(int i = offset.intValue() ; i < rankingList.size()+offset.intValue(); i++) {
			rankingList.get(i).setRankingIndex(i+1);
		}
		
		
		
		return rankingList;
	}
	
	@Override
	public CrewTotalRecordDto getMyTotalRecord(UserEntity userEntity) throws Exception{
		QCrewTotalRecordEntity qCrewTotalRecordEntity = QCrewTotalRecordEntity.crewTotalRecordEntity;
//		BooleanBuilder builder = new BooleanBuilder();
		
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
				.where(QCrewTotalRecordEntity.crewTotalRecordEntity.userEntity.userSeq.eq(userEntity.getUserSeq()))
				.fetchOne();
	}

}
