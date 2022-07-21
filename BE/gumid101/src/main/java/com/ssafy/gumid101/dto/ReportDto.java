package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.ReportEntity;

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

	private long reportSeq;
	
	private String reportContent;
	
	private String reportStatus;
	
	private long reportCrewBoardSeq;
	
	public static ReportDto of(ReportEntity report) {
		return new ReportDtoBuilder()
				.reportSeq(report.getReportSeq())
				.reportContent(report.getReportContent())
				.reportStatus(report.getReportStatus())
				.reportCrewBoardSeq(report.getReportCrewBoardSeq())
				.build();
	}
	

}