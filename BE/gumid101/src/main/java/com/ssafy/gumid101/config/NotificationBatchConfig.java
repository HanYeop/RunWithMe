package com.ssafy.gumid101.config;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.entity.FCMEntity;
import com.ssafy.gumid101.firebase.FirebaseMessageStoreUtil;
import com.ssafy.gumid101.firebase.FirebaseMessageUtil;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class NotificationBatchConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final int CHUNK_SIZE = 10;
	private final FirebaseMessageStoreUtil fcmStore;

	private final FirebaseMessageUtil fcmUtil;
	
	@Bean("notificationJob")
	@Qualifier("notificationJob")
	public Job notificationJop() {

		Job exampleJob = jobBuilderFactory.get("notificationJop").start(notificationStep()).build();
		return exampleJob;
	}
	
	@Bean
	@JobScope // 잡이 관리하는 영역
	public Step notificationStep() {
		log.debug("포인트 정산 JOB을 시작합니다.");
		return stepBuilderFactory.get("notificationStep").<FCMEntity, FCMEntity>chunk(CHUNK_SIZE)
				.reader(notificationReader())
				.processor(notificationProcessor())
				.writer(notificationWriter())
				
				.build();
	}
	
	@Bean
	@StepScope
	public JpaPagingItemReader<FCMEntity> notificationReader(){
		log.debug("알림 기능 JOB-Reader 과정을 시작합니다.");
		
		return new JpaPagingItemReaderBuilder<FCMEntity>().pageSize(CHUNK_SIZE)
				.queryString(
						"SELECT f FROM FCMEntity f"
						)
				.entityManagerFactory(entityManagerFactory).name("notificationReader").build();
		
	}
	@Bean
	@StepScope
	public ItemProcessor<FCMEntity, FCMEntity> notificationProcessor(){
		log.debug("알림 기능 JOB-Processor 과정을 시작합니다.");
		
		return new ItemProcessor<FCMEntity, FCMEntity>() {
			
			@Override
			public FCMEntity process(FCMEntity item) throws Exception {
				
				fcmUtil.sendMessageTo(item.getToken(), item.getTitle(), item.getContent());
				
				return item;
			}
		};
	}
	
	
	@Bean
	@StepScope
	public ItemWriter<FCMEntity> notificationWriter(){
		
		return new ItemWriter<FCMEntity>() {

			@Override
			public void write(List<? extends FCMEntity> items) throws Exception {
				
				List<FCMEntity> fcmEntities = (List<FCMEntity>)items;
				fcmStore.deleteFcmMessage(fcmEntities);
				
			}
		};
		
	}
}
