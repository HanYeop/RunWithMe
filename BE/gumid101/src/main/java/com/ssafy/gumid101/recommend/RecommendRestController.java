package com.ssafy.gumid101.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.LatLngParamsDto;

@RestController
@RequestMapping("/recommend")
public class RecommendRestController {
	@Autowired
	private RecommendService recommendService;
	
	/**
	 * lat : 위도 (상하)
	 * lng : 경도 (좌우)
	 * @param latlngParams
	 * @return
	 */
	@GetMapping("/boards")
	public RequestEntity<?> getRecommend(@ModelAttribute LatLngParamsDto latlngParams){
		
		
		return null;
	}
	
	@PostMapping("/board")
	public RequestEntity<?> writeRecommend(@RequestParam long run_record_seq, @RequestParam(required = false) int hard_point, @RequestParam(required = false) int environment_point){
		
		return null;
	}
	
	@DeleteMapping("recommend/boards/{trackBoardId}")
	public RequestEntity<?> deleteRecommend(@PathVariable long run_record_seq){
		return null;
	}
}
