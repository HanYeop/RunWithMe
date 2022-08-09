package com.ssafy.gumid101.customercenter;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.dto.QuestionDto;
import com.ssafy.gumid101.dto.ReportDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
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

	@ApiOperation("게시글 신고")
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
