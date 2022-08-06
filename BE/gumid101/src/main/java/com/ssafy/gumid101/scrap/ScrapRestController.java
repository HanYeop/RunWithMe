package com.ssafy.gumid101.scrap;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.res.ScrapInfoDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
@Api(tags = "스크랩 관련 컨트롤러")
public class ScrapRestController {
	private final ScrapService scrapServ;
	
	/**
	 * 토큰으로 부터 유저 DTO 로드
	 * 
	 * @return
	 */
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	@ApiOperation("추천 경로 스크랩하기")
	@PostMapping("/{trackBoardSeq}")
	public ResponseEntity<?> scrapTrackBoard(@PathVariable Long trackBoardSeq, @RequestParam(required = false) Optional<String> title) throws Exception {
		UserDto userDto = loadUserFromToken();
		ScrapInfoDto siDto = scrapServ.scrapTrackBoard(userDto.getUserSeq(), trackBoardSeq, title.orElse(""));
		return new ResponseEntity<>(new ResponseFrame<>(true, siDto, 1, "스크랩을 완료했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation("추천 경로 스크랩 삭제하기 (사실상 success가 결과입니다. data가 false로 가는 경우는 없..)")
	@DeleteMapping("/{scrapSeq}")
	public ResponseEntity<?> deleteScrap(@PathVariable Long scrapSeq) throws Exception {
		UserDto userDto = loadUserFromToken();
		Boolean deleteResult = scrapServ.deleteScrap(userDto.getUserSeq(), scrapSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, deleteResult, 1, "스크랩을 삭제했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation("스크랩 목록 보기")
	@GetMapping("/")
	public ResponseEntity<?> getMyScrap(@RequestParam(required = false) Optional<String> title) throws Exception {
		UserDto userDto = loadUserFromToken();
		List<ScrapInfoDto> siList = scrapServ.getScraps(userDto.getUserSeq(), title.orElse(""));
		return new ResponseEntity<>(new ResponseFrame<>(true, siList, 1, "자신의 스크랩 목록 검색에 성공했습니다."), HttpStatus.OK);
	}
	
	
}
