package com.ssafy.gumid101.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/check/{nickname}")
	public ResponseEntity<?> checkDupNickName(@PathVariable String nickname) {
//		return new ResponseEntity<Boolean>(userService.checkDupNickname(nickname), HttpStatus.OK);
		return null;
	}
}
