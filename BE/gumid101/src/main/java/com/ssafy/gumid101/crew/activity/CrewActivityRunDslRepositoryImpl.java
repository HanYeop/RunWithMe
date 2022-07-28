package com.ssafy.gumid101.crew.activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.entity.QRunRecordEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;

@Repository
public class CrewActivityRunDslRepositoryImpl implements CrewActivityRunDslRepository{
	
	@Autowired
	JPAQueryFactory factory;
	
	@Override
	public List<RunRecordEntity> getRunRecords(RecordParamsDto condition){
		
		QRunRecordEntity qRunRecordEntity = QRunRecordEntity.runRecordEntity;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		if (condition.getCrewSeq() != null && condition.getCrewSeq() > 0) {
			builder.and(qRunRecordEntity.crewEntity.crewSeq.eq(condition.getCrewSeq()));
		}
		if (condition.getUserSeq() != null && condition.getUserSeq() > 0) {
			builder.and(qRunRecordEntity.userEntity.userSeq.eq(condition.getUserSeq()));
		}
		if (condition.getMonth() != null && condition.getMonth() != 0) {
			builder.and(qRunRecordEntity.runRecordRegTime.month().eq(condition.getMonth()));
		}
		if (condition.getYear() != null && condition.getYear() != 0) {
			builder.and(qRunRecordEntity.runRecordRegTime.year().eq(condition.getYear()));
		}
		if(condition.getMaxRunRecordSeq() != null && condition.getMaxRunRecordSeq() != 0) {
			builder.and(qRunRecordEntity.runRecordSeq.lt(condition.getMaxRunRecordSeq()));
		}
		
		JPAQuery<RunRecordEntity> jpaQuery =  factory.selectFrom(qRunRecordEntity).where(builder)
				.orderBy(qRunRecordEntity.runRecordSeq.desc(),qRunRecordEntity.runRecordRegTime.desc());
		
		
		if(condition.getSize() != null && condition.getSize() >= 0) {
			jpaQuery.limit(condition.getSize());
		}
		
		//maxRunRecordSeq
		
		return jpaQuery.fetch();
	}

}