package com.ssafy.gumid101.customercenter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.gumid101.req.QuestionReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerCenterCustomRepositoryImpl implements CustomerCenterCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	
	@Override
	public void selectQuestionByQuestionReqDto(QuestionReqDto params) {
		// TODO Auto-generated method stub
		
	}

}
