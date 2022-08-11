package com.ssafy.gumid101.dailycheck;

import org.springframework.stereotype.Service;

@Service
public interface DailyCheckService {

	/**
	 * 
	 * @param userSeq
	 * @return 오늘 체크 안 해서 체크 가능하면 true, 해서 체크 불가능하면 false;
	 * @throws Exception 
	 */
	Boolean isCheckable(Long userSeq) throws Exception;

	Boolean dailyChecking(Long userSeq) throws Exception;

	Integer monthResult(Long userSeq, Long year, Long month);

}
