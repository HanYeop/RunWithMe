package com.ssafy.gumid101.req;

import javax.validation.constraints.NotNull;

import com.ssafy.gumid101.customercenter.QuestionStatus;

public class QuestionSelectParameter {

	private String title;
	private String questionSeq;
	private DateOder dateOrder;
	private QuestionStatus status;
	

	private int pageItemSize; //페이안에 있는 아이템 갯수

	private int currentPage;  //현재 요청페이지

	private int pageNaviSize; 
	
	
	public int getPageNaviSize() {
		return pageNaviSize;
	}

	public void setPageNaviSize(int pageNaviSize) {
		this.pageNaviSize = pageNaviSize;
	}

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

	public int getPageItemSize() {
		return pageItemSize;
	}

	public void setPageItemSize(int pageItemSize) {
		this.pageItemSize = pageItemSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	
	
	
}
