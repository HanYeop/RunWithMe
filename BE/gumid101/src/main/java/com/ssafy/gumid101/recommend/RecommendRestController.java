package com.ssafy.gumid101.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommend")
public class RecommendRestController {
	@Autowired
	private RecommendService recommendService;
	
	@GetMapping("/boards")
	public RequestEntity<?> getRecommend(){
		/**
		 * 좌우상하 위/경도를 받는 dto 필요
		 */
		
		return null;
	}
	
	@PostMapping("/board")
	public RequestEntity<?> writeRecommend(
			){
		return null;
	}
	
	public RequestEntity<?> deleteRecommend(){
		return null;
	}
}
