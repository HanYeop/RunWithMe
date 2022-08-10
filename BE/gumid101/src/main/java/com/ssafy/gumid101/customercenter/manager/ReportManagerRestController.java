package com.ssafy.gumid101.customercenter.manager;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.customercenter.CustomerCenterService;
import com.ssafy.gumid101.customercenter.ReportStatus;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
import com.ssafy.gumid101.req.ReportSelectReqDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequestMapping("/customer-center/manager")
@RequiredArgsConstructor
@RestController
public class ReportManagerRestController {

	private final ReportManagerService reportManagerService;



	//질문글 상세보기 
	//신고글 상세보기는 나중에
	
	/*
	// 질문글 스테이트 바꾸기
	@ApiOperation("질문글 상태 변경")
	@PatchMapping("/manager/qustion/{questionseq}")
	public ResponseEntity<?> setQuestiontState(@PathVariable("question") Long questionSeq,@RequestBody Map<String,String>  param){
		
		String status = param.get("questionStatus");
		
		int result = customerCenterService.updateQustionStatus(questionSeq,QuestionStatus.valueOf(status));
		
		ResponseFrame<Integer> res = ResponseFrame.of(result, result, result == 1 ? "정상적으로 질문글 상태가 변경되었습니다.":"질문글 상태 변경이 실패하였습니다.");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
*/
	
	@ApiOperation("신고글  상태 별 총개")
	@GetMapping("/reports-state-count")
	public ResponseEntity<?> getReportStateCount(){
		
		Map<String, Long> result = reportManagerService.getReportStateCount();

		long totalCount =(long) result.values().stream().
				reduce(0L, (prev,cur)->cur+prev);
		
		ResponseFrame<Map<String, Long>> res = ResponseFrame.of(result,(int)totalCount,"총 갯수와 각각 상태의 갯수를 반환합니다.");
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@ApiOperation("신고글 상세보기")
	@GetMapping("/reports/{reportqeq}")
	public ResponseEntity<?> getDetailReports(@PathVariable("reportseq") Long reportSeq){
		
		ResponseFrame<Integer> res = null;
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
		
		
	// 신고글 스테이트 바꾸기
	@ApiOperation("신고글 상태 변경")
	@PutMapping("/reports/{reportseq}")
	public ResponseEntity<?> setReportState(@PathVariable("reportseq") Long reportSeq,@RequestBody Map<String,String>  param){
		
		String status = param.get("reportStatus");
		
		int result = reportManagerService.updateReportsStatus(reportSeq,ReportStatus.valueOf(status));
		
		ResponseFrame<Integer> res = ResponseFrame.of(result, result,  result == 1 ? "정상적으로 신고글 상태가 변경되었습니다.":"신고글 상태 변경이 실패하였습니다.");
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// 신고글 조회
	@ApiOperation("신고글 조회 (관리자)")
	@GetMapping("/reports")
	public ResponseEntity<?> getReports(@ModelAttribute ReportSelectReqDto params, BindingResult bindingResult)
			throws Exception {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			throw new IllegalParameterException(errors.get(0).getDefaultMessage());
		}

		

		Map<String, Object> resultMap =reportManagerService.selectReportsByParam(params);

		ResponseFrame<Map<String, Object>> res = ResponseFrame.of(resultMap, 0, "신고글과 페이징 정보를 반환합니다.");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	/*
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
	*/

	// 질문글 삭제
	/*
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
	*/

	
	// 신고 크루 게시글 삭제
	@ApiOperation("신고 크루 게시글 삭제 (관리자)")
	@DeleteMapping("/crew-boards/{boardseq}")
	public ResponseEntity<?> deleteReportCrewBoard(@PathVariable("boardseq") String boardSeq) throws Exception {
		Long seq;
		try {

			seq = Long.parseLong(boardSeq);
		} catch (Exception e) {
			throw new IllegalParameterException(String.format("입력 파라미터 에러 : %s", boardSeq));
		}
		int result = reportManagerService.deleteCrewBoard(seq);
		String msg = result == 0 ? "게시글 삭제에 실패하였습니다." : "해당 게시글이 삭제되었습니다.";
		ResponseFrame<Integer> res = ResponseFrame.of(result, result, msg);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 신고 게시글 삭제
	@ApiOperation("신고글 삭제 (관리자)")
	@DeleteMapping("/reports/{reportseq}")
	public ResponseEntity<?> deleteReport(@PathVariable("reportseq") String reportSeq) throws Exception {
		Long seq = null;
		try {

			seq = Long.parseLong(reportSeq);
		} catch (Exception e) {
			throw new IllegalParameterException(String.format("입력 파라미터 에러 : %s", seq));
		}

		int result = reportManagerService.deleteReport(seq);

		String msg = result == 0 ? "신고글 삭제에 실패하였습니다." : "신고글이 삭제되었습니다.";

		ResponseFrame<Integer> res = ResponseFrame.of(result, result, msg);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
