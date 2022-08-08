package com.ssafy.gumid101.dailycheck;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.entity.DailyCheckEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyCheckServiceImpl implements DailyCheckService {
	
	private final DailyCheckRepository dailyRepo;
	
	/**
	 * 
	 * @param userSeq
	 * @return 오늘 체크 안 해서 체크 가능하면 true, 해서 체크 불가능하면 false;
	 * @throws Exception 
	 */
	@Override
	public Boolean isCheckable(Long userSeq) throws Exception {
		LocalDateTime dayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		LocalDateTime dayEnd = dayStart.plusDays(1).minusSeconds(1);
		if(dailyRepo.findByUserSeqAndDailyCheckTimeBetween(userSeq, dayStart, dayEnd).isPresent()) {
			throw new DuplicateException("이미 체크했습니다.");
		}
		else {
			return true;
		}
	}
	
	@Transactional
	@Override
	public Boolean dailyChecking(Long userSeq) throws Exception {
		if (!isCheckable(userSeq)) {
			throw new DuplicateException("이미 체크했습니다.");
		}
		DailyCheckEntity dcEntity = DailyCheckEntity.builder() //
				.userSeq(userSeq) //
				.build(); //
		dailyRepo.save(dcEntity);
		
		/**
		 * 보상주는 로직 여기에 짜야함.
		 */
		
		return true;
	}
	
	@Override
	public Integer monthResult(Long userSeq, Long year, Long month) {
		LocalDateTime monthStart = LocalDateTime.of(year.intValue(), month.intValue(), 1, 0, 0, 0);
		LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);
		return dailyRepo.countByUserSeqAndDailyCheckTimeBetween(userSeq, monthStart, monthEnd).intValue();
	}
}
