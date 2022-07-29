package com.ssafy.gumid101.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssafy.gumid101.crew.manager.CrewManagerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrewSchedule {
	
	private final CrewManagerService cmServ;
	
	// 초 분 시 일 월 요일
	@Scheduled(cron = "0 0 3 * * *")
	// 크루 포인트 정산 메소드
	public void crewPointDistribute() throws Exception {
		log.info("종료된 크루의 포인트 정산을 시도합니다.");
		try {
			for(Long crewSeq : cmServ.getFinishAndNonDistributeCrews()) {
				try {
					cmServ.crewFinishPoint(crewSeq);
				}catch (Exception e) {
					log.debug("========================");
					log.debug(crewSeq.toString());
					log.debug(e.getMessage());
					log.debug(crewSeq.toString());
					log.debug("========================");
				}
			}
		}catch (Exception e) {
			log.debug(e.getMessage());
		}
		log.info("종료된 크루의 포인트 정산을 끝냅니다.");
	}
}
