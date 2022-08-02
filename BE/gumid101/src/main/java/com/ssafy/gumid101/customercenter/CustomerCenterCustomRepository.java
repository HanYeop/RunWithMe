package com.ssafy.gumid101.customercenter;

import com.ssafy.gumid101.req.QuestionReqDto;

public interface CustomerCenterCustomRepository {

	void selectQuestionByQuestionReqDto(QuestionReqDto params);
	
}
