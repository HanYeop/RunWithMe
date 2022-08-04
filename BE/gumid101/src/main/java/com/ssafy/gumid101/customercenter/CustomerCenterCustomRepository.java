package com.ssafy.gumid101.customercenter;

import java.util.List;

import com.ssafy.gumid101.req.QuestionReqDto;
import com.ssafy.gumid101.req.QuestionSelectParameter;
import com.ssafy.gumid101.req.ReportSelectReqDto;
import com.ssafy.gumid101.res.QuestionResDto;
import com.ssafy.gumid101.res.ReportResDto;

public interface CustomerCenterCustomRepository {

	public List<QuestionResDto> selectQuestionByParam(QuestionSelectParameter params);
	public Long selectCountQuestionByParam(QuestionSelectParameter params) ;
	public List<ReportResDto> selectReportsByParam(ReportSelectReqDto params);
	public Long selectCountReportsByParam(ReportSelectReqDto params) ;
}
