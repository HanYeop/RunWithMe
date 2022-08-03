package com.ssafy.gumid101;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ssafy.gumid101.crew.manager.CrewManagerRepository;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@DataJpaTest
@AutoConfigureTestDatabase
public class DateTest {

	@Autowired
	CrewManagerRepository crewManagerRepo;
	
	
	@Test
	public void timecheck() {
		System.out.println("kk");

	}
}
