package com.ssafy.gumid101.req;

public class QuestionSelectParameter {

	private String title;
	private String questionSeq;
	private DateOder dateOrder;
	
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
	
	
	
	
}
