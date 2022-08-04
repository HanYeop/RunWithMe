package com.ssafy.gumid101.customercenter;

import java.util.Map;

import org.springframework.http.RequestEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.QuestionDto;
import com.ssafy.gumid101.dto.ReportDto;
import com.ssafy.gumid101.req.QuestionReqDto;
import com.ssafy.gumid101.req.QuestionSelectParameter;
import com.ssafy.gumid101.req.ReportSelectReqDto;

public interface CustomerCenterService {

	QuestionDto postQuestion(QuestionDto questionDto, Long userSeq) throws Exception;

	ReportDto postReport(Long boardSeq, String reportContent, Long userSeq) throws Exception;

	int deleteCrewBoard(Long boardSeq) throws Exception;

	int deleteReport(Long seq)throws Exception;

	int deleteQuestion(Long seq)throws Exception;

	int answerQuestion(Long seq, QuestionReqDto questionReqDto, MultipartFile[] files)throws Exception;

	public Map<String, Object> selectQuestion(QuestionSelectParameter params);

	Map<String, Object> selectReportsByParam(ReportSelectReqDto params);

	int updateReportsStatus(Long reportId, ReportStatus status);

	int updateQustionStatus(Long questionSeq, QuestionStatus valueOf);


}
