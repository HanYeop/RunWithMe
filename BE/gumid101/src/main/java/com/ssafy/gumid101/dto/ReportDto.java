package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.ReportEntity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto implements Serializable {

	private Long reportSeq;

	@ApiParam(value = "신고 내용")
	private String reportContent;

	@ApiParam(value = "신고 처리 상태")
	private String reportStatus;

	@ApiParam(value = "신고한 글 번호")
	private Long reportCrewBoardSeq;
	
	public static ReportDto of(ReportEntity report) {
		
		if(report == null)
			return null;
		return new ReportDtoBuilder()
				.reportSeq(report.getReportSeq())
				.reportContent(report.getReportContent())
				.reportStatus(report.getReportStatus())
				.reportCrewBoardSeq(report.getReportCrewBoardSeq())
				.build();
	}
	

}