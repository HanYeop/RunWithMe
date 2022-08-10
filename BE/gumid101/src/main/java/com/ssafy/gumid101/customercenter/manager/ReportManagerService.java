package com.ssafy.gumid101.customercenter.manager;

import java.util.Map;

import com.ssafy.gumid101.customercenter.ReportStatus;
import com.ssafy.gumid101.req.ReportSelectReqDto;

public interface ReportManagerService {

	int deleteCrewBoard(Long boardSeq) throws Exception;

	int deleteReport(Long seq)throws Exception;
	 Map<String, Long> getReportStateCount() ;

	Map<String, Object> selectReportsByParam(ReportSelectReqDto params);

	int updateReportsStatus(Long reportId, ReportStatus status);

	Map<String, Object> selectReportById(Long reportSeq);
}
