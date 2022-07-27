package com.ssafy.gumid101.customercenter;

import com.ssafy.gumid101.dto.QuestionDto;
import com.ssafy.gumid101.dto.ReportDto;
import com.ssafy.gumid101.entity.QuestionEntity;
import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.entity.UserEntity;

public class CustomerCenterServiceImpl implements CustomerCenterService {

	ReportRepository reportRepository;
	QuestionRepository  questionRepository;
	@Override
	public QuestionDto postQuestion(QuestionDto questionDto, Long userSeq) throws Exception {
	
		QuestionEntity question = QuestionEntity.builder()
				.questionContent(null)
				.questionStatus(QuestStatus.WAITING)
				.build();
		
		question.setUserEntity(UserEntity.builder().userSeq(userSeq).build());
		
		questionRepository.save(question);
		
		return QuestionDto.of(question);
	}

	@Override
	public ReportDto postReport(long boardSeq, String reportContent, Long userSeq) throws Exception {
		
		ReportEntity reportEntity = ReportEntity.builder()
		.reportContent(reportContent)
		.reportCrewBoardSeq(boardSeq)
		.reportStatus(null)
		.userReporterEntity(UserEntity.builder().userSeq(userSeq).build()).build();
		
		reportRepository.save(reportEntity);
		
		return ReportDto.of(reportEntity);
	}

}
