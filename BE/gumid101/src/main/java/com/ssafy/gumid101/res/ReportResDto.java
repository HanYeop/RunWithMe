package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.customercenter.ReportStatus;

import lombok.Data;

@Data
public
class ReportResDto {
	private Long reportSeq;
	private String reportContent;
	private ReportStatus reportState;
	private Long crewBoardSeq;
	private Long reporterUserSeq;
	private String reporterNickName;
	private Long targetUserSeq;
	private String targetNincName;
	private LocalDateTime regTime;
}