package com.ssafy.gumid101.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.ssafy.gumid101.customercenter.QuestionStatus;

public class QuestionSelectParameter extends PagingParameter{

	private String title;
	private String questionSeq;
	private DateOder dateOrder;
	private QuestionStatus status;

	

	public QuestionSelectParameter() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestionSeq() {
		return questionSeq;
	}

	public void setQuestionSeq(String questionSeq) {
		this.questionSeq = questionSeq;
	}

	public DateOder getDateOrder() {
		return dateOrder;
	}

	public void setDateOrder(DateOder dateOrder) {
		this.dateOrder = dateOrder;
	}

	public QuestionStatus getStatus() {
		return status;
	}

	public void setStatus(QuestionStatus status) {
		this.status = status;
	}


	
	
	
	
}
