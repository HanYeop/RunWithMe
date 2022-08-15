package com.ssafy.gumid101.competition;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.entity.QCompetitionUserRecordEntity;
import com.ssafy.gumid101.entity.QImageFileEntity;
import com.ssafy.gumid101.entity.QUserEntity;
import com.ssafy.gumid101.res.RankingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CompetitionRankingRepositoryImpl implements CompetitionRankingRepository{
	private final JdbcTemplate jdbcTemplate;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<RankingDto> getCompetitionRankingList(Long size, Long offset) {

		QCompetitionUserRecordEntity qUserRecord = new QCompetitionUserRecordEntity("t_cur");
		QUserEntity userEntity = new QUserEntity("t_user");
		QImageFileEntity imgEntity = new QImageFileEntity("t_img");
		// user crew 키를 identity로 각 통합랭킹을 관리하는 테이블에서 user의 통합랭킹을 얻기 위하여
		// user로 그룹핑을 실행

		if (size == null) {
			size = (long) Integer.MAX_VALUE;
		}

		if (offset == null) {
			offset = 0L;
		}

		List<RankingDto> rankings = jpaQueryFactory.from(qUserRecord)
				.select(Projections.fields(RankingDto.class, //
						userEntity.nickName.as("userName"), //
						userEntity.userSeq.as("userSeq"), //
						userEntity.competitionResult.as("competitionResult"), //
						qUserRecord.competitionDistance.as("rankingValue"), //
						imgEntity.imgSeq.coalesce(0L).as("imgSeq") //
						)) //
				.leftJoin(qUserRecord.userEntity, userEntity) //
				.leftJoin(userEntity.imageFile, imgEntity) //
				.orderBy(qUserRecord.competitionDistance.desc(), userEntity.userSeq.asc()) //
				.offset(offset) //
				.limit(offset + size) //
				.fetch();
		if (rankings.isEmpty()) {
			return rankings;
		}
		int nowRank = offset.intValue() + 1;
		int stack = 0;
		int beforeValue = rankings.get(0).getRankingValue();
		for (int i = offset.intValue(); i < rankings.size() + offset.intValue(); i++) {
			if (rankings.get(i).getRankingValue() == beforeValue) {				
				stack++;
			}
			else {
				nowRank += stack;
				stack = 1;
			}
			rankings.get(i).setRankingIndex(nowRank);				
		}

		return rankings;
	}
	

	@Override
	public RankingDto getCompetitionUserRanking(Long competitionSeq, Long userSeq) {
		RankingDto myRanking = null;
		String sql = "select ranking, targetField, subr2.user_seq as user_seq, user_nickname, t_user.img_seq as img_seq, t_user.competition_result as competitionResult "
				+ "FROM ("
				+ "SELECT ranking, user_seq, targetField "
					+ "FROM ("
						+ "SELECT ROW_NUMBER() OVER (order by competition_distance) as ranking, user_seq, competition_distance as targetField "
						+ "FROM	t_competition_user_record as tctc "
						+ "where tctc.competition_seq = ?"
					+ ") "
				+ "as subr "
				+ "where subr.user_seq = ?) "
				+ "as subr2 "
				+ "inner join t_user on t_user.user_seq = subr2.user_seq;";

		try {
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, new Object[] {competitionSeq, userSeq});

			myRanking = new RankingDto();

			myRanking.setRankingIndex(((BigInteger) resultMap.get("ranking")).intValue());
			myRanking.setRankingValue((Integer) resultMap.get("targetField"));
			myRanking.setUserName((String) resultMap.get("user_nickname"));
			myRanking.setCompetitionResult(resultMap.get("competitionResult") != null ? 
					CompetitionResultStatus.valueOf((String) resultMap.get("competitionResult")) : 
						CompetitionResultStatus.NONRANKED);
			myRanking.setUserSeq((Long) resultMap.get("user_seq"));
			myRanking.setImgSeq((Long) resultMap.get("img_seq"));
			if(myRanking.getImgSeq() == null) {
				myRanking.setImgSeq(0L);
			}
		} catch (Exception e) {
			log.info("전체 랭킹 조회,거리,시간 조회 쿼리에서의 익셉션 : " + e.getMessage());
			myRanking = new RankingDto();
			myRanking.setRankingIndex(0);
			myRanking.setRankingValue(0);
			myRanking.setUserName("");
			myRanking.setUserSeq(userSeq);
			myRanking.setImgSeq(0L);
		}

		return myRanking;
	}
}
