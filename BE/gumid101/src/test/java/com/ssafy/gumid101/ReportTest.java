package com.ssafy.gumid101;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.gumid101.customercenter.ReportRepository;


@AutoConfigureTestDatabase
@DataJpaTest
public class ReportTest {

	
	@Autowired
	ReportRepository reportRepo;
	
	@Test
	@DisplayName("신고글 상태 갯수 잘 나오는지 확인")
	public void givenNoneWhenCallReposrtStatusCountThengetCount() {
		
		//List<Map<String, Object>>  result = reportRepo.getReportStateCountThoughtGroupBy();
		
		System.out.println("qwe");
	}
}
