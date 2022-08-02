package com.ssafy.gumid101.customercenter;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.crew.activity.CrewActivityBoardRepository;
import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.QuestionDto;
import com.ssafy.gumid101.dto.ReportDto;
import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.QuestionEntity;
import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerCenterServiceImpl implements CustomerCenterService {

	private final ReportRepository reportRepository;
	private final QuestionRepository questionRepository;
	private final UserRepository userRepo;
	private final CrewActivityBoardRepository boardRepo;
	
	@Override
	public QuestionDto postQuestion(QuestionDto questionDto, Long userSeq) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));
		QuestionEntity question = QuestionEntity.builder()
				.questionContent(questionDto.getQuestionContent())
				.questionStatus(QuestionStatus.WAITING)
				.userEntity(userEntity)
				.build();
		
		questionRepository.save(question);
		
		return QuestionDto.of(question);
	}

	@Override
	public ReportDto postReport(Long boardSeq, String reportContent, Long userSeq) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));
		
		if (reportRepository.findByUserReporterEntityAndReportCrewBoardSeq(userEntity, boardSeq).isPresent()) {
			throw new DuplicateException("이미 신고한 게시글입니다.");
		}
		
		CrewBoardEntity crewBoardEntity = boardRepo.findById(boardSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 글이 존재하지 않습니다."));
		if (userEntity.getUserSeq() == crewBoardEntity.getUserEntity().getUserSeq()) {
			throw new IllegalParameterException("자기 자신의 글은 신고할 수 없습니다.");
		}
		
		ReportEntity reportEntity = ReportEntity.builder()
				.reportContent(reportContent)
				.reportCrewBoardSeq(boardSeq)
				.reportStatus(ReportStatus.WAITING)
				.userReporterEntity(userEntity).build();
		
		reportRepository.save(reportEntity);
		
		return ReportDto.of(reportEntity);
	}

}
