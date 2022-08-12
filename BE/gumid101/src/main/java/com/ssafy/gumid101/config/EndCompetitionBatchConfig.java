package com.ssafy.gumid101.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssafy.gumid101.competition.CompetitionRepository;
import com.ssafy.gumid101.competition.CompetitionResultStatus;
import com.ssafy.gumid101.competition.CompetitionUserRecordRepository;
import com.ssafy.gumid101.entity.CompetitionEntity;
import com.ssafy.gumid101.entity.CompetitionUserRecordEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.firebase.FirebaseMessageStoreUtil;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class EndCompetitionBatchConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final int CHUNK_SIZE = 10;
	private final CompetitionRepository competitionRepo;
	private final UserRepository userRepo;
	private final CompetitionUserRecordRepository competitionUserRecordRepo;
	private final FirebaseMessageStoreUtil fcmStore;

	@Bean("competitionEndJob")
	@Qualifier("competitionEndJob")
	public Job competitionEndJop() {

		Job exampleJob = jobBuilderFactory.get("competitionEndJop").start(competitionCalculatingStep()).build();
		return exampleJob;
	}
	
	@Bean
	@JobScope // 잡이 관리하는 영역
	public Step competitionCalculatingStep() {
		log.debug("시즌제 대회 정산 JOB을 시작합니다.");
		return stepBuilderFactory.get("pointCalculatingStep").<CompetitionEntity, CompetitionEntity>chunk(CHUNK_SIZE)
				.reader(endCompetitionReader(null))
				.processor(endCompetitionPointCalculateProcessor())
				.writer(competitionWriter())
				.build();
	}
	

	@Bean
	@StepScope
	public JpaPagingItemReader<CompetitionEntity> endCompetitionReader(
			@Value("#{jobParameters[requestDate]}") final String requestDate) {
		log.debug("시즌제 대회 정산 JOB-Reader 과정을 시작합니다.");
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("checkYn", "N");
		parameterValues.put("nowTime", LocalDateTime.now());
		return new JpaPagingItemReaderBuilder<CompetitionEntity>().pageSize(CHUNK_SIZE).parameterValues(parameterValues)
				.queryString(
						"SELECT c FROM CompetitionEntity c where c.checkYn = :checkYn AND c.competitionDateEnd < :nowTime")
				.entityManagerFactory(entityManagerFactory).name("crewPointCalculationReader").build();
	}
	
	@Bean
	@StepScope
	public ItemProcessor<CompetitionEntity, CompetitionEntity> endCompetitionPointCalculateProcessor() {

		return new ItemProcessor<CompetitionEntity, CompetitionEntity>() {

			@Override
			public CompetitionEntity process(CompetitionEntity competitionEntity) throws Exception {
				log.debug(competitionEntity.getCompetitionSeq() + "번 대회 " + competitionEntity.getCompetitionName() + "의 정산 JOB-Process 과정을 시작합니다.");
				userRepo.initCompetitionResultAsBulk();
				List<CompetitionUserRecordEntity> recordList = competitionUserRecordRepo.findByCompetitionEntityOrderByCompetitionDistanceDesc(competitionEntity);
				int idx = 0;
				if (idx < recordList.size()) {
					UserEntity first = recordList.get(idx).getUserEntity();
					// 1등 보상
					first.setPoint(first.getPoint() + recordList.get(idx).getCompetitionDistance() / 10);
					first.setCompetitionResult(CompetitionResultStatus.FIRST);
					idx++;
				}
				if (idx < recordList.size()) {
					UserEntity second = recordList.get(idx).getUserEntity();
					// 2등 보상
					second.setPoint(second.getPoint() + recordList.get(idx).getCompetitionDistance() / 20);
					second.setCompetitionResult(CompetitionResultStatus.SECOND);
					idx++;
				}
				if (idx < recordList.size()) {
					UserEntity third = recordList.get(idx).getUserEntity();
					// 3등 보상
					third.setPoint(third.getPoint() + recordList.get(idx).getCompetitionDistance() / 30);
					third.setCompetitionResult(CompetitionResultStatus.THIRD);
					idx++;
				}
				while (idx < recordList.size() && idx < recordList.size() * 10 / 100) {
					UserEntity tenPercent = recordList.get(idx).getUserEntity();
					// 순위권 제외 10% 안쪽 보상
					tenPercent.setPoint(tenPercent.getPoint() + recordList.get(idx).getCompetitionDistance() / 50);
					tenPercent.setCompetitionResult(CompetitionResultStatus.UP10PERCENT);
					idx++;		
				}
				competitionEntity.setCheckYn("Y");
				return competitionEntity;
			}

		};
	}

	@Bean
	@StepScope // 스텝이 보는 영역
	JpaItemWriter<CompetitionEntity> competitionWriter() {
		log.debug("시즌제 대회 정산 JOB-Writer 과정을 시작합니다.");

		return new JpaItemWriterBuilder<CompetitionEntity>().entityManagerFactory(entityManagerFactory).build();
	}
}
