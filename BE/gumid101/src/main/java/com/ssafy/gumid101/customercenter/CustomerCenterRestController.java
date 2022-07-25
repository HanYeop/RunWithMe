package com.ssafy.gumid101.customercenter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.QuestionDto;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/customer-center")
public class CustomerCenterRestController {
	
	@ApiOperation("질문 글 등록(미구현)")
	@PostMapping("/qeustion")
	public ResponseEntity<?> writeQuestion(@RequestBody QuestionDto questionDto){
		return null;
	}
	
	
	@ApiOperation("게시글 신고(미구현)")
	@PostMapping("/report/board/{boardSeq}")
	public ResponseEntity<?> reportCrewBoard(@PathVariable long boardSeq, @RequestParam(required = false) String report_content){
		return null;
	}
	
}
