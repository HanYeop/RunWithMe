package com.ssafy.gumid101.res;

import com.ssafy.gumid101.customercenter.QuestionStatus;

import lombok.Data;

@Data
public class QuestionResDto{
	
	
	private String content;
	private String title;
	private String regTime;
	private QuestionStatus status;
	private Long questionSeq;
	private Long userSeq;
	private  String userState;
	private String nickName;
	private String email;

	
	
}