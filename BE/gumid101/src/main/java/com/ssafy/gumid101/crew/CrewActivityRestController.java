package com.ssafy.gumid101.crew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;

@RestController
@RequestMapping("/crew-activity")
public class CrewActivityRestController {
	
	private CrewActivityService crewActivityService;
	
	public RequestEntity<?> getCrewRecord(){
		return null;
	}
	
	@GetMapping("/{crewSeq}/records")
	public RequestEntity<?> getCrewRecordList(@ModelAttribute RecordParamsDto recordParamsDto){
		return null;
	}
	
	@GetMapping("/{crewSeq}/ranking")
	public RequestEntity<?> getCrewRankings(@ModelAttribute RankingParamsDto rankingParamsDto){
		return null;
	}

	@GetMapping("/{crewSeq}/my")
	public RequestEntity<?> getCrewMyRecords(@ModelAttribute RecordParamsDto recordParamsDto){
		return null;
	}
	
	@GetMapping("/{crewSeq}/my-total")
	public RequestEntity<?> getCrewMyTotalRecords(@PathVariable long crewSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/board")
	public RequestEntity<?> writeCrewBoards(@PathVariable long crewSeq, @RequestPart(name = "img", required = false) MultipartFile image, @RequestPart CrewBoardDto crewBoardDto){
		
		return null;
	}
	
	@GetMapping("/{crewSeq}/boards")
	public RequestEntity<?> getCrewBoards(@PathVariable long crewSeq){
		return null;
	}
	
	@DeleteMapping("/{crewSeq}/boards/{boardSeq}")
	public RequestEntity<?> deleteCrewBoards(@PathVariable long crewSeq, @PathVariable long boardSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/records")
	public RequestEntity<?> writeRunRecord(@PathVariable long crewSeq, @RequestPart(name = "img") MultipartFile image, @RequestPart RunRecordDto runRecordDto){
		
		return null;
	}
}
