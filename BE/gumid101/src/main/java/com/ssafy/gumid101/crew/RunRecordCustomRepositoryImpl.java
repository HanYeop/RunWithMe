package com.ssafy.gumid101.crew;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.dto.CoordinateDto;
import com.ssafy.gumid101.dto.RecordCoordinateDto;
import com.ssafy.gumid101.entity.QRecordCoordinateEnitity;
import com.ssafy.gumid101.entity.RecordCoordinateEnitity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RunRecordCustomRepositoryImpl implements RunRecordCustomRepository {

	private final JdbcTemplate jdbcTemplate;
	private final JPAQueryFactory jpaQueryFactory;
	
	@Override
	public int[] coordinatesInsertBatch(Long recordSeq, List<CoordinateDto> coordinates) {

		
		int[] results = jdbcTemplate.batchUpdate(
				"insert into t_record_coordinate(run_record_seq, latitude,longitude) " + "values(?,?,?)",
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, recordSeq);
						ps.setDouble(2, coordinates.get(i).getLatitude());
						ps.setDouble(3, coordinates.get(i).getLongitude());
					}

					@Override
					public int getBatchSize() {
						return coordinates.size();
					}
				});
		
		
		return results;
	}
	
	public List<RecordCoordinateDto> getCoordinateByRunRecordSeq(Long recordSeq) {
		/*
		 * 	private Long coordinateSeq;
	private double latitude;
	private double longtitude;
		 */
		QRecordCoordinateEnitity rc = new QRecordCoordinateEnitity("rc");
		
		List<RecordCoordinateDto>  recordCoordinateList = jpaQueryFactory.from(rc).where(rc.runRecord.runRecordSeq.eq(recordSeq))
		.orderBy(rc.coordinateSeq.asc()).select(Projections.fields(RecordCoordinateDto.class, 
				rc.coordinateSeq.as("coordinateSeq"),
				rc.latitude.as("latitude"),
				rc.longitude.as("longitude")
				)).fetch();
		
		
		return recordCoordinateList;
	}

}
