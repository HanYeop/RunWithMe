package com.ssafy.gumid101.totalranking;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.entity.QCrewTotalRecordEntity;
import com.ssafy.gumid101.entity.QImageFileEntity;
import com.ssafy.gumid101.entity.QUserEntity;
import com.ssafy.gumid101.res.RankingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CrewTotalRecordCustomRepositoryImpl implements CrewTotalRecordCustomRepository {

	// 자신의 랭킹 구하기를 위해서 어쩔 수 없이 사용
	private final JdbcTemplate jdbcTemplate;
	private final JPAQueryFactory jpaQueryFactory;

	public List<RankingDto> getUserTotalRankingOptionType(String type, Long size, Long offset) {

		QCrewTotalRecordEntity crewTotal = new QCrewTotalRecordEntity("t_ctr");
		QUserEntity userEntity = new QUserEntity("t_user");
		QImageFileEntity imgEntity = new QImageFileEntity("t_img");
		// user crew 키를 identity로 각 통합랭킹을 관리하는 테이블에서 user의 통합랭킹을 얻기 위하여
		// user로 그룹핑을 실행
		NumberExpression<?> typeField = null;

		if ("distance".equals(type)) {
			typeField = crewTotal.totalDistance.sum();
		} else if ("time".equals(type)) {

			typeField = crewTotal.totalTime.sum();
		}

		if (size == null) {
			size = (long) Integer.MAX_VALUE;
		}

		if (offset == null) {
			offset = 0L;
		}

		List<RankingDto> rankings = jpaQueryFactory.from(crewTotal)
				
				.select(Projections.fields(RankingDto.class, userEntity.nickName.as("userName"),
						userEntity.userSeq.as("userSeq"), typeField.as("rankingValue"),imgEntity.imgSeq.coalesce(0L).as("imgSeq")))
				.leftJoin(crewTotal.userEntity,userEntity)
				.leftJoin(userEntity.imageFile,imgEntity)
				.groupBy(userEntity.userSeq).orderBy(typeField.desc(),userEntity.userSeq.asc())
				.offset(offset).limit(offset + size)
				.fetch();

		for (int i = offset.intValue(); i < rankings.size() + offset.intValue(); i++) {
			rankings.get(i).setRankingIndex(i + 1);
		}

		return rankings;
	}

	public RankingDto getMyTotalRanking(String type, Long userSeq) {
		RankingDto myRanking = null;
		String targetField = "total_distance";
		if ("distance".equals(type)) {
			targetField = "total_distance";
		} else if ("time".equals(type)) {
			targetField = "total_time";
		} else if ("point".equals(type)) {
			return getMyRankingPost(userSeq);
		} else {
			System.out.println("에러 잡아주세요.TotalRankingRestController 기능의 CrewTotalRecordCustom Repository");
			return null;
		}

		String sql = "select ranking,targetField,subr2.user_seq as user_seq,user_nickname,t_img.img_seq as img_seq FROM " + "(SELECT "
				+ "ranking,total_calorie, total_distance, total_longest_distance, total_longest_time, total_record_reg_time, total_time, crew_seq, user_seq,targetField "
				+ "FROM ( "
				+ String.format(
						"SELECT ROW_NUMBER() OVER (order by %s DESC,user_seq ASC) as ranking,tct.*,sum(%s) as targetField FROM t_crew_total_record as tct GROUP BY user_seq ",
						targetField,targetField)
				+ ") as subr " + "where subr.user_seq = ? " + ") as subr2 " 
						+ "inner join t_user as t_user "
				+ "on t_user.user_seq = subr2.user_seq "
				+ "inner join t_img " + 
				"on t_img.img_seq = t_user.img_seq ";

		try {
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, userSeq);

			myRanking = new RankingDto();

			myRanking.setRankingIndex(((BigInteger) resultMap.get("ranking")).intValue());
			myRanking.setRankingValue(((BigDecimal) resultMap.get("targetField")).intValue());
			myRanking.setUserName((String) resultMap.get("user_nickname"));
			myRanking.setUserSeq((Long) resultMap.get("user_seq"));
			myRanking.setImgSeq((Long)resultMap.get("img_seq"));
		}catch (Exception e) {
			log.info("전체 랭킹 조회,거리,시간 조회 쿼리에서의 익셉션 : "+e.getMessage());
			myRanking = new RankingDto();
			myRanking.setRankingIndex(0);
			myRanking.setRankingValue(0);
			myRanking.setUserName("");
			myRanking.setUserSeq(userSeq);
			myRanking.setImgSeq(0L);
		}

		
		return myRanking;
	}

	private RankingDto getMyRankingPost(Long userSeq) {
		RankingDto myRanking = new RankingDto();
		String sql = "WITH sub1 AS( "
				+ "SELECT ROW_NUMBER() OVER (order by t.user_point desc,user_seq) as ranking,t.* FROM t_user as t "
				+ ")  " + "SELECT ranking,user_point,user_nickname,user_seq,img_seq FROM sub1 WHERE sub1.user_seq = ? ";

		Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, userSeq);

		myRanking.setRankingIndex(((BigInteger) resultMap.get("ranking")).intValue());
		myRanking.setRankingValue((Integer) resultMap.get("user_point"));
		myRanking.setUserName((String) resultMap.get("user_nickname"));
		myRanking.setUserSeq((Long) resultMap.get("user_seq"));
		myRanking.setImgSeq((Long)resultMap.get("img_seq"));
		return myRanking;
	}
}
