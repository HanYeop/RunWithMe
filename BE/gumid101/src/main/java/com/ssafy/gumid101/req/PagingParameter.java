package com.ssafy.gumid101.req;

import javax.validation.constraints.Min;

import org.springframework.web.bind.annotation.RequestParam;

public class PagingParameter {

	@Min(value = 0,message = "페이지 아이템 갯수는 1이상이어야 합니다.")
	private int pageItemSize; //페이안에 있는 아이템 갯수
	@Min(value = 0,message ="요청 페이지는 1이상 이어야합니다.")
	private int currentPage;  //현재 요청페이지
	@Min(value = 0,message = "페이지 네비게이션 사이즈는 1이상이어야 합니다.")
	private int pageNaviSize;
	
	
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
	public int getPageNaviSize() {
		return pageNaviSize;
	}
	public void setPageNaviSize(int pageNaviSize) {
		this.pageNaviSize = pageNaviSize;
	} 
	
	
}
