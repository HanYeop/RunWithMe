package com.ssafy.gumid101.customercenter;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.QuestionDto;
import com.ssafy.gumid101.dto.ReportDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
import com.ssafy.gumid101.req.QuestionReqDto;
import com.ssafy.gumid101.req.QuestionSelectParameter;
import com.ssafy.gumid101.req.ReportSelectReqDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "고객센터")
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer-center")
public class CustomerCenterRestController {

	private final CustomerCenterService customerCenterService;
	private final RedisService redisServ;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	//질문글 상세보기 
	//신고글 상세보기는 나중에
	
	// 질문글 스테이트 바꾸기
	@ApiOperation("질문글 상태 변경")
	@PatchMapping("/manager/qustion/{questionseq}")
	public ResponseEntity<?> setQuestiontState(@PathVariable("question") Long questionSeq,@RequestBody Map<String,String>  param){
		
		String status = param.get("questionStatus");
		
		int result = customerCenterService.updateQustionStatus(questionSeq,QuestionStatus.valueOf(status));
		
		ResponseFrame<Integer> res = ResponseFrame.of(result, result, result == 1 ? "정상적으로 질문글 상태가 변경되었습니다.":"질문글 상태 변경이 실패하였습니다.");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 신고글 스테이트 바꾸기
	@ApiOperation("신고글 상태 변경")
	@PatchMapping("/manager/reports/{reportseq}")
	public ResponseEntity<?> setReportState(@PathVariable("reportseq") Long reportSeq,@RequestBody Map<String,String>  param){
		
		String status = param.get("reportStatus");
		
		int result = customerCenterService.updateReportsStatus(reportSeq,ReportStatus.valueOf(status));
		
		ResponseFrame<Integer> res = ResponseFrame.of(result, result,  result == 1 ? "정상적으로 신고글 상태가 변경되었습니다.":"신고글 상태 변경이 실패하였습니다.");
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// 신고글 조회
	@ApiOperation("신고글 조회 (관리자)")
	@GetMapping("/manager/reports")
	public ResponseEntity<?> getReports(@ModelAttribute ReportSelectReqDto params, BindingResult bindingResult)
			throws Exception {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			throw new IllegalParameterException(errors.get(0).getDefaultMessage());
		}

		

		Map<String, Object> resultMap =customerCenterService.selectReportsByParam(params);

		ResponseFrame<Map<String, Object>> res = ResponseFrame.of(resultMap, 0, "신고글과 페이징 정보를 반환합니다.");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 질문 글 조회
	@ApiOperation("질문 글 조회 (관리자)")
	@GetMapping("/manager/questions")
	public ResponseEntity<?> getQuestions(@ModelAttribute QuestionSelectParameter params, BindingResult bindingResult)
			throws Exception {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			throw new IllegalParameterException(errors.get(0).getDefaultMessage());
		}

		Map<String, Object> resultMap = customerCenterService.selectQuestion(params);

		ResponseFrame<Map<String, Object>> res = ResponseFrame.of(resultMap, 0, "질문글과 페이징 정보를 반환합니다.");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 질문글 답변
	@ApiOperation("질문 글 답변 (관리자)")
	@PostMapping("/manager/questions/{qeustionseq}")
	public ResponseEntity<?> answerQuestion(@PathVariable("qeustionseq") String questionSeq,
			QuestionReqDto questionReqDto, MultipartFile[] files) throws Exception {
		Long seq = null;

		try {

			seq = Long.parseLong(questionSeq);
		} catch (Exception e) {
			throw new IllegalParameterException(String.format("입력 파라미터 에러 : %s", questionSeq));
		}

		int result = customerCenterService.answerQuestion(seq, questionReqDto, files);

		String msg = result == 0 ? "질문글 답변을 실패하였습니다." : "질문글 답변이 완료되었습니다.";

		ResponseFrame<Integer> res = ResponseFrame.of(result, result, msg);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 질문글 삭제
	@ApiOperation("질문글 삭제 (관리자)")
	@DeleteMapping("/manager/questions/{questionseq}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("boardseq") String questionSeq) throws Exception {
		Long seq = null;
		try {

			seq = Long.parseLong(questionSeq);
		} catch (Exception e) {
			throw new IllegalParameterException(String.format("입력 파라미터 에러 : %s", questionSeq));
		}
		int result = customerCenterService.deleteQuestion(seq);

		String msg = result == 0 ? "질문글 삭제를 실패하였습니다." : "질문글이 삭제되었습니다.";

		ResponseFrame<Integer> res = ResponseFrame.of(result, result, msg);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 신고 크루 게시글 삭제
	@ApiOperation("신고 크루 게시글 삭제 (관리자)")
	@DeleteMapping("/manager/crew-boards/{boardseq}")
	public ResponseEntity<?> deleteReportCrewBoard(@PathVariable("boardseq") String boardSeq) throws Exception {
		Long seq;
		try {

			seq = Long.parseLong(boardSeq);
		} catch (Exception e) {
			throw new IllegalParameterException(String.format("입력 파라미터 에러 : %s", boardSeq));
		}
		int result = customerCenterService.deleteCrewBoard(seq);
		String msg = result == 0 ? "게시글 삭제에 실패하였습니다." : "해당 게시글이 삭제되었습니다.";
		ResponseFrame<Integer> res = ResponseFrame.of(result, result, msg);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 신고 게시글 삭제
	@ApiOperation("신고글 삭제 (관리자)")
	@DeleteMapping("/manager/reports/{reportseq}")
	public ResponseEntity<?> deleteReport(@PathVariable("reportseq") String reportSeq) throws Exception {
		Long seq = null;
		try {

			seq = Long.parseLong(reportSeq);
		} catch (Exception e) {
			throw new IllegalParameterException(String.format("입력 파라미터 에러 : %s", seq));
		}

		int result = customerCenterService.deleteReport(seq);

		String msg = result == 0 ? "신고글 삭제에 실패하였습니다." : "신고글이 삭제되었습니다.";

		ResponseFrame<Integer> res = ResponseFrame.of(result, result, msg);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	//////////////////////////////////////////////////// 이 위로는 관리자 기능

	@ApiOperation("질문 글 등록")
	@PostMapping("/qeustion")
	public ResponseEntity<?> writeQuestion(@RequestBody QuestionDto questionDto, @ApiIgnore BindingResult bindingResult)
			throws Exception {

		if (bindingResult.hasErrors()) {

		}

		UserDto userDto = loadUserFromToken();

		redisServ.getIsUseable(userDto.getUserSeq().toString() + "writeQuestion" + questionDto.getQuestionContent(),
				10);

		QuestionDto questionResultDto = customerCenterService.postQuestion(questionDto, userDto.getUserSeq());

		ResponseFrame<QuestionDto> res = ResponseFrame.of(questionResultDto, 1, "질문이 등록되었습니다.");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@ApiOperation("게시글 신고(미구현)")
	@PostMapping("/report/board/{boardSeq}")
	public ResponseEntity<?> reportCrewBoard(@ApiParam("신고글 아이디") @PathVariable long boardSeq,
			@ApiParam("신고 이유") @RequestBody(required = false) String reportContent) throws Exception {

		UserDto userDto = loadUserFromToken();

		redisServ.getIsUseable(userDto.getUserSeq().toString() + "reportCrewBoard" + boardSeq, 10);

		ReportDto result = customerCenterService.postReport(boardSeq, reportContent, userDto.getUserSeq());

		ResponseFrame<ReportDto> res = ResponseFrame.of(result, 1, "신고가 등록되었습니다.");

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

}
