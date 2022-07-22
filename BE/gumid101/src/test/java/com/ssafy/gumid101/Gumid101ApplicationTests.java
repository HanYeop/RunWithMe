package com.ssafy.gumid101;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.gumid101.user.UserRepository;

@DataJpaTest
class Gumid101ApplicationTests {

	@Autowired
	UserRepository userRepo;
	
	@Test
	void contextLoads() {
 assertNotNull(userRepo);
	}

}
