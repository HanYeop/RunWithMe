package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.customercenter.ReportStatus;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportResDto {
	private Long reportSeq;
	private String reportContent;
	private ReportStatus reportState;
	private Long crewBoardSeq;
	private Long reporterUserSeq;
	private String reporterNickName;
	private Long targetUserSeq;
	private String targetNickName;
	private LocalDateTime regTime;

	public static class ReportResDtoBuilder {

		private ReportResDto reportRes;

		public ReportResDtoBuilder reportSeq(Long value) {
			reportRes.setReportSeq(value);
			return this;
		}

		public ReportResDtoBuilder reportContent(String content) {
			reportRes.setReportContent(content);
			return this;
		}

		public ReportResDtoBuilder reportState(ReportStatus state) {
			reportRes.setReportState(state);
			return this;
		}

		public ReportResDtoBuilder crewBoardSeq(Long value) {
			reportRes.setCrewBoardSeq(value);
			return this;
		}

		public ReportResDtoBuilder reporterUserSeq(Long value) {
			reportRes.setReporterUserSeq(value);
			return this;
		}

		public ReportResDtoBuilder reporterNickName(String nickname) {
			reportRes.setReporterNickName(nickname);
			return this;
		}

		public ReportResDtoBuilder targetUserSeq(Long value) {
			reportRes.setTargetUserSeq(value);
			return this;
		}

		public ReportResDtoBuilder targetNickName(String nickname) {
			reportRes.setTargetNickName(nickname);
			return this;
		}

		public ReportResDtoBuilder regTime(LocalDateTime ldt) {
			reportRes.setRegTime(ldt);
			return this;
		}

		public ReportResDto build() {
			return reportRes;
		}
	}

	public static ReportResDtoBuilder builder() {
		ReportResDtoBuilder builder = new ReportResDtoBuilder();
		builder.reportRes = new ReportResDto();
		return builder;
	}
}