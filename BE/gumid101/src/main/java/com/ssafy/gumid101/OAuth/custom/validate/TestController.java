package com.ssafy.gumid101.OAuth.custom.validate;

import java.io.IOException;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.jwt.JwtUtilsService;
import com.ssafy.gumid101.user.UserRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class AK{
	String code;
}


@Controller
public class TestController {

	@Autowired
	private  UserRepository userRepo;
	@Autowired
	private JwtUtilsService jwtUtilSevice;
	
	@ResponseBody
	@PostMapping("/test/register")
	public ResponseEntity<?> register(@ModelAttribute UserDto userDto){
		UserEntity user = UserEntity.builder().email(userDto.getEmail())
				.nickName(userDto.getNickName())
				.height(userDto.getHeight())
				.weight(userDto.getWeight()).build();
		
		userRepo.save(user);
		
		userDto.setRole(user.getRole());
		userDto.setUserSeq(user.getUserSeq());
		
		String token = jwtUtilSevice.createToken(userDto);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("msg", "개발자용 토큰발급");
		map.put("j-token-develope",token);
		return new ResponseEntity<>(map,org.springframework.http.HttpStatus.OK);
	}
	
	
	@PostMapping("/test")
	public String tset(@ModelAttribute AK a) throws GeneralSecurityException, IOException {
		Map map = new GoogleTokenValidate().validate(a.code);
		
		return "뭐";
	}
	
}
