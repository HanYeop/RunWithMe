package com.ssafy.gumid101.crew;

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

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;

@RestController
@RequestMapping("/crew-manager")
public class CrewManagerRestController {
	
	@Autowired
	private CrewManagerService crewManagerService;
	
	@GetMapping("/my-current-crew")
	public RequestEntity<?> getMyCurrentCrew(){
		return null;
	}
	
	@GetMapping("/my-end-crew")
	public RequestEntity<?> getMyEndCrew(){
		return null;
	}
	
	@GetMapping("/recruitment")
	public RequestEntity<?> getCrewRecruitment(@ModelAttribute RecruitmentParamsDto paramsDto){
		
		return null;
	}
	
	@PostMapping("/crew")
	public RequestEntity<?> createCrew(@ModelAttribute CrewDto crewDto){
		
		return null;
	}
	
	@DeleteMapping("/crew/{crewSeq}")
	public RequestEntity<?> deleteCrew(@PathVariable long crewSeq){
		return null;
	}
	
	@GetMapping("/crew/{crewSeq}")
	public RequestEntity<?> getCrewDetail(@PathVariable long crewSeq){
		return null;
	}
	
	@PostMapping("/crew/{crewSeq}/user")
	public RequestEntity<?> joinCrew(@PathVariable long crewSeq, @RequestParam String password){
		return null;
	}
	
	@DeleteMapping("/crew/{crewSeq}/user")
	public RequestEntity<?> exitCrew(@PathVariable long crewSeq){
		return null;
	}
}
