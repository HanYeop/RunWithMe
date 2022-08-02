package com.ssafy.gumid101.OAuth.custom.validate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.gumid101.crew.manager.CrewManagerService;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.firebase.FcmMessage;
import com.ssafy.gumid101.firebase.FcmMessage.Message;
import com.ssafy.gumid101.firebase.FirebaseMessage;
import com.ssafy.gumid101.jwt.JwtUtilsService;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.schedule.CrewSchedule;
import com.ssafy.gumid101.user.Role;
import com.ssafy.gumid101.user.UserRepository;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class AK {
	String code;
}

@Controller
public class TestController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CrewManagerService cmServ;
	@Autowired
	private JwtUtilsService jwtUtilSevice;

	@Autowired
	private FirebaseMessage firebaseMessage;
	
	@Autowired
	private CrewSchedule cs;
	@ApiOperation(value = "크루 종료 배치 테스트")
	@GetMapping("/batchtest")
	public ResponseEntity<?> batchtest() throws Exception {


		cs.crewPointDistribute();
		ResponseFrame<?> res = ResponseFrame.of(true, 0, "스프링 배치 시작");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@ApiOperation(value = "크루 분배하기")
	@GetMapping("/{crewSeq}/distribute")
	public ResponseEntity<?> crewMemberCheck(@PathVariable Long crewSeq) throws Exception {

		Boolean check = cmServ.crewFinishPoint(crewSeq);

		ResponseFrame<?> res = ResponseFrame.of(check, 0, "크루 정산시도만 완료");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/test/register")
	public ResponseEntity<?> register(@ModelAttribute UserDto userDto) {
		UserEntity user = UserEntity.builder().email(userDto.getEmail()).nickName(userDto.getNickName())
				.height(userDto.getHeight()).weight(userDto.getWeight()).build();

		userRepo.save(user);

		userDto.setRole(user.getRole());
		userDto.setUserSeq(user.getUserSeq());

		String token = jwtUtilSevice.createToken(userDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "개발자용 토큰발급");
		map.put("j-token-develope", token);
		return new ResponseEntity<>(map, org.springframework.http.HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/test/register/temp")
	public ResponseEntity<?> register(@RequestParam String email) {

		UserEntity user = UserEntity.builder().email(email).role(Role.TEMP).build();

		String token = jwtUtilSevice.createToken(UserDto.of(user));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "개발자용 토큰발급");
		map.put("j-token-develope", token);
		return new ResponseEntity<>(map, org.springframework.http.HttpStatus.OK);
	}

	@PostMapping("/test")
	public String tset(@ModelAttribute AK a) throws GeneralSecurityException, IOException {
		Map map = new GoogleTokenValidate().validate(a.code);

		return "뭐";
	}

	@ResponseBody
	@GetMapping("/test/get/{userId}")
	public String tset(@PathVariable Long userId) {

		System.out.println("kkk");

		return jwtUtilSevice.createToken(UserDto.of(userRepo.findById(userId).orElse(null)));
	}

	
	@ResponseBody
	@GetMapping("/test/notification")
	public String notificationTest() throws IOException{
		
		
		List<Message> fcmMesageList = userRepo.findAll().stream().map((userEntity)->{
			return FcmMessage.ofMessage(userEntity.getFcmToken(), "테스트", "안녕하세요\n ㅎㅎ");
		}).collect(Collectors.toList());
		
		firebaseMessage.sendMessageTo(fcmMesageList);
		
		return "알림테스트";
	}
}
