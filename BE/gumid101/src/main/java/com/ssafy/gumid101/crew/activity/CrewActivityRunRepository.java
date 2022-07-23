package com.ssafy.gumid101.crew.activity;

import java.util.ArrayList
;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.entity.QRunRecordEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;

@Repository
public class CrewActivityRunRepository{
	
	@Autowired
	EntityManager em;
	
	@Autowired
	JPAQueryFactory factory;
	
	public List<RunRecordDto> getRunRecords(RecordParamsDto condition){
		QRunRecordEntity qRunRecordEntity = QRunRecordEntity.runRecordEntity;
		BooleanBuilder builder = new BooleanBuilder();
		if (condition.getCrewSeq() != null && condition.getCrewSeq() == -1) {
			builder.and(qRunRecordEntity.crewEntity.crewSeq.eq(condition.getCrewSeq()));
		}
		if (condition.getUserSeq() != null && condition.getUserSeq() == -1) {
			builder.and(qRunRecordEntity.userEntity.userSeq.eq(condition.getUserSeq()));
		}
		if (condition.getMonth() != null && condition.getMonth() == 0) {
			builder.and(qRunRecordEntity.runRecordRegTime.month().eq(condition.getMonth()));
		}
		if (condition.getYear() != null && condition.getYear() == 0) {
			builder.and(qRunRecordEntity.runRecordRegTime.year().eq(condition.getYear()));
		}
		factory = new JPAQueryFactory(em);
		List<RunRecordEntity> fetch = factory.selectFrom(qRunRecordEntity).where(builder)
				.limit(condition.getSize() == null || condition.getSize() <= 0 ? 10 : condition.getSize())
				.offset(condition.getOffset() == null? 0 : condition.getOffset()).fetch();
		List<RunRecordDto> dtos = new ArrayList<>(fetch.size());
		for(RunRecordEntity recEnt : fetch) {
			dtos.add(RunRecordDto.of(recEnt));
		}
		return dtos;
	}

}