package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.QuestionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto implements Serializable {

	private Long questionSeq;
	
	private String questionContent;
	
	private String questionStatus;
	
	public static QuestionDto of(QuestionEntity question) {
		return new QuestionDtoBuilder()
				.questionSeq(question.getQuestionSeq())
				.questionContent(question.getQuestionContent())
				.questionStatus(question.getQuestionStatus())
				.build();
	}
	

}