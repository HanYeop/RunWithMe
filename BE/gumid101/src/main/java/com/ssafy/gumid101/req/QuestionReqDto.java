package com.ssafy.gumid101.req;

import java.util.List;


public class QuestionReqDto {

	public QuestionReqDto() {
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public List<Integer> getFilePositionList() {
		return filePositionList;
	}



	public void setFilePositionList(List<Integer> filePositionList) {
		this.filePositionList = filePositionList;
	}



	//답변 제목
	private String title;
	
	//답변 내용
	private String content;
	
	
	
	private List<Integer> filePositionList; //이미지 넣을 때 row를 저장한다.
}
