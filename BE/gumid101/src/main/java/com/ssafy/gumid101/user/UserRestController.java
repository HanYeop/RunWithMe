package com.ssafy.gumid101.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.UserDto;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	private UserService userService;
	
	@GetMapping("/check/{nickname}")
	public ResponseEntity<?> checkDupNickName(@PathVariable String nickname) {
//		return new ResponseEntity<Boolean>(userService.checkDupNickname(nickname), HttpStatus.OK);
		return null;
	}
	
	@PostMapping("/profile")
	public ResponseEntity<?> setMyProfile(@RequestBody UserDto userDto) {
		
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto)autentication.getPrincipal();
		
		return new ResponseEntity<String>("qwe",HttpStatus.OK);
	}
}
