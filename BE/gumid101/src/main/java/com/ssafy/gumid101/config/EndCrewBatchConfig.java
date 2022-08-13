package com.ssafy.gumid101.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.FCMEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.firebase.FirebaseMessageStoreUtil;
import com.ssafy.gumid101.firebase.FirebaseMessageUtil;
import com.ssafy.gumid101.user.UserRepository;

import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class EndCrewBatchConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final int CHUNK_SIZE = 10;
	private final RunRecordRepository runRepo;
	private final UserCrewJoinRepository userCrewJoinRepo;
	private final FirebaseMessageStoreUtil fcmStore;
	private final UserRepository userRepo;
	
	
	@Bean("crewEndJob")
	@Qualifier("crewEndJob")
	public Job crewEndJop() {

		Job exampleJob = jobBuilderFactory.get("crewEndJop").start(pointCalculatingStep()).build();
		return exampleJob;
	}

	@Bean
	@JobScope // 잡이 관리하는 영역
	public Step pointCalculatingStep() {
		log.debug("포인트 정산 JOB을 시작합니다.");
		return stepBuilderFactory.get("pointCalculatingStep").<CrewEntity, CrewEntity>chunk(CHUNK_SIZE)
				.reader(endCrewReader(null))
				.processor(endCrewPointCalculateProcessor())
				.writer(calculatedPintWriter())
				.build();

	}

	@Bean
	@StepScope
	public JpaPagingItemReader<CrewEntity> endCrewReader(
			@Value("#{jobParameters[requestDate]}") final String requestDate) {
		log.debug("포인트 정산 JOB-Reader 과정을 시작합니다.");
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("crewCheckYn", "N");
		parameterValues.put("nowTime", LocalDateTime.now());
		return new JpaPagingItemReaderBuilder<CrewEntity>().pageSize(CHUNK_SIZE).parameterValues(parameterValues)
				.queryString(
						"SELECT c FROM CrewEntity c where c.crewCheckYn = :crewCheckYn AND c.crewDateEnd < :nowTime ORDER BY c.crewSeq")
				.entityManagerFactory(entityManagerFactory).name("crewPointCalculationReader").build();
		// 아직 처리가 안됬고 크루 종료일이 끝난 크루를 부른다.
	}

	@Bean
	@StepScope
	public ItemProcessor<CrewEntity, CrewEntity> endCrewPointCalculateProcessor() {

		return new ItemProcessor<CrewEntity, CrewEntity>() {

			@Override
			public CrewEntity process(CrewEntity crewEntity) throws Exception {
				log.debug("포인트 정산 JOB-Process 과정을 시작합니다.");
				List<UserCrewJoinEntity> userCrewList = userCrewJoinRepo.findAllByCrewEntity(crewEntity);

				// 참가비가 0이거나 크루원 수가 0인 경우는 정산할 필요 없다.
				if (crewEntity.getCrewCost() == null || crewEntity.getCrewCost() <= 0 || userCrewList.size() == 0) {
					crewEntity.setCrewCheckYn("Y");
					return crewEntity;
				}

				Map<Long, Long> userSucceedDays = new HashMap<>();

				// 크루 전체의 기록을 가져온다.
				List<RunRecordEntity> crewRunRecords = runRepo
						.findByCrewEntityAndRunRecordCompleteYNOrderByRunRecordStartTime(crewEntity, "Y");

				// 전체 참가비 합계
				Long totalPoint = (long) userCrewList.size() * crewEntity.getCrewCost();
				long totalSucceedDay = 0;
				// 시작과 끝 간격을 1주일로 함
				LocalDateTime weeksEnd = crewEntity.getCrewDateStart().plusDays(6).plusHours(23).plusMinutes(59)
						.plusSeconds(59);
				int idx = 0;
				while (idx < crewRunRecords.size() && !weeksEnd.isAfter(crewEntity.getCrewDateEnd())) {
					Map<Long, Long> weekSucceedDays = new HashMap<>();
					while (idx < crewRunRecords.size()
							&& !crewRunRecords.get(idx).getRunRecordStartTime().isAfter(weeksEnd)) {
						long userSeq = crewRunRecords.get(idx).getUserEntity().getUserSeq();
						if (!weekSucceedDays.containsKey(userSeq)) {
							weekSucceedDays.put(userSeq, 0L);
						}
						if (weekSucceedDays.get(userSeq) < (long) crewEntity.getCrewGoalDays()) {
							weekSucceedDays.put(userSeq, weekSucceedDays.get(userSeq) + 1);
							if (!userSucceedDays.containsKey(userSeq)) {
								userSucceedDays.put(userSeq, 0L);
							}
							userSucceedDays.put(userSeq, userSucceedDays.get(userSeq) + 1);
							totalSucceedDay++;
						}
						idx++;
					}
					weeksEnd = weeksEnd.plusDays(7);
				}

				// 크루원 중 그 누구도 하루도 못 한 경우는 정산해주지 않는다.
				if (totalSucceedDay <= 0) {
					crewEntity.setCrewCheckYn("Y");
					return crewEntity;
				}

				UserEntity user = null;
				for (int i = 0; i < userCrewList.size(); i++) {
					if (userSucceedDays.containsKey(userCrewList.get(i).getUserEntity().getUserSeq())) {
						// 주어진 조건 내에서 계산결과는 Integer범위에서 안 벗어남. (심지어 괄호 내부계산은 long형이다.)
						user = userCrewList.get(i).getUserEntity();

						int point = (int) (totalPoint * userSucceedDays.get(userCrewList.get(i).getUserEntity().getUserSeq())
										/ totalSucceedDay);

						userRepo.updatePointAsBulk(user.getUserSeq(),point);
						
						if (Strings.hasLength(user.getFcmToken())) {
							// Message message = FcmMessage.ofMessage(user.getFcmToken(), "RunWithMe [포인트
							// 정산]", String.format("%s 크루가 종료되어 포인트를 정산받았습니다.\n정산된 포인트: %d",
							// userCrewList.get(i).getCrewEntity().getCrewName(),point));
							fcmStore.storeFcmMessage(user.getFcmToken(), "RunWithMe [포인트 정산]",
									String.format("%s 크루가 종료되어 포인트를 정산받았습니다.\n정산된 포인트: %d",
											userCrewList.get(i).getCrewEntity().getCrewName(), point),
									user.getUserSeq());
						}

					}
				}
				crewEntity.setCrewCheckYn("Y");
				return crewEntity;
			}

		};
	}

	@Bean
	@StepScope // 스텝이 보는 영역
	JpaItemWriter<CrewEntity> calculatedPintWriter() {
		log.debug("포인트 정산 JOB-Writer 과정을 시작합니다.");

		return new JpaItemWriterBuilder<CrewEntity>().entityManagerFactory(entityManagerFactory).build();
	}

	

}



