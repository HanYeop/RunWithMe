package com.ssafy.gumid101.crew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;

@RestController
@RequestMapping("/crew-activity")
public class CrewActivityRestController {
	
	@Autowired
	private CrewActivityService crewActivityService;
	
	public RequestEntity<?> getCrewRecord(){
		return null;
	}
	
	@GetMapping("/{crewSeq}/records")
	public RequestEntity<?> getCrewRecordList(RecordParamsDto recordParamsDto){
		return null;
	}
	
	@GetMapping("/{crewSeq}/ranking")
	public RequestEntity<?> getCrewRankings(RankingParamsDto rankingParamsDto){
		return null;
	}

	@GetMapping("/{crewSeq}/my")
	public RequestEntity<?> getCrewMyRecords(RecordParamsDto recordParamsDto){
		return null;
	}
	
	@GetMapping("/{crewSeq}/my-total")
	public RequestEntity<?> getCrewMyTotalRecords(@PathVariable long crewSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/board")
	public RequestEntity<?> writeCrewBoards(@PathVariable long crewSeq,
			@RequestParam String content
			){
		/**
		 * 여기서 이미지도 받아야 함.
		 */
		
		return null;
	}
	
	@GetMapping("/{crewSeq}/boards")
	public RequestEntity<?> getCrewBoards(){
		return null;
	}
	
	@DeleteMapping("/{crewSeq}/boards/{boardSeq}")
	public RequestEntity<?> deleteCrewBoards(@PathVariable long crewSeq, @PathVariable long boardSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/records")
	public RequestEntity<?> writeRunRecord(@PathVariable long crewSeq){
		/**
		 * 러닝 기록 dto 만들어서 받아야 함
		 */
		
		return null;
	}
}
