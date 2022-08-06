package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.ScrapEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScrapDto implements Serializable {

	private Long scrapSeq;
	
	private String scrapTitle;

//	@ApiParam(value = "달린 사람 번호")
//	private Long runnerSeq;
//	
//	@ApiParam(value = "달린 사람 닉네임")
//	private String runnerNickName;
	
	public static ScrapDto of(ScrapEntity scrapEntity) {
		if (scrapEntity == null) {
			return ScrapDto.builder() //
					.scrapSeq(0L) //
					.scrapTitle("") //
//					.runnerSeq(0L) //
//					.runnerNickName("") //
					.build(); //
		}
		return ScrapDto.builder() //
				.scrapSeq(scrapEntity.getScrapSeq()) //
				.scrapTitle(scrapEntity.getScrapTitle()) //
//				.runnerSeq(scrapEntity.getTrackBoardEntity().getRunRecordEntity().getUserEntity().getUserSeq()) //
//				.runnerNickName(scrapEntity.getTrackBoardEntity().getRunRecordEntity().getUserEntity().getNickName()) //
				.build();
	}
	

}