package com.ssafy.gumid101.customercenter;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.QuestionDto;
import com.ssafy.gumid101.dto.ReportDto;
import com.ssafy.gumid101.req.QuestionReqDto;

public interface CustomerCenterService {

	QuestionDto postQuestion(QuestionDto questionDto, Long userSeq) throws Exception;

	ReportDto postReport(Long boardSeq, String reportContent, Long userSeq) throws Exception;

	int deleteCrewBoard(Long boardSeq) throws Exception;

	int deleteReport(Long seq)throws Exception;

	int deleteQuestion(Long seq)throws Exception;

	int answerQuestion(Long seq, QuestionReqDto questionReqDto, MultipartFile[] files)throws Exception;

	void selectQuestion(QuestionReqDto params);


}
