package com.ssafy.gumid101.customercenter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.QuestionDto;

@RestController
@RequestMapping("/customer-center")
public class CustomerCenterRestController {
	
	@PostMapping("/qeustion")
	public ResponseEntity<?> writeQuestion(@RequestBody QuestionDto questionDto){
		return null;
	}
	
	@PostMapping("/report/board/{boardSeq}")
	public ResponseEntity<?> reportCrewBoard(@PathVariable long boardSeq, @RequestParam(required = false) String report_content){
		return null;
	}
	
}
