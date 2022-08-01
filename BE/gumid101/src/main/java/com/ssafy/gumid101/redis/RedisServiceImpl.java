package com.ssafy.gumid101.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{
	
	private final StringRedisTemplate stringRedisTemplate;
	
	@Override
	public Boolean getRedisStringValue(String key, Integer delayTime) {
		ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
		if (stringValueOperations.get(key) == null) {
//			System.out.println("null이었음!");
			stringValueOperations.set(key, "yet", delayTime, TimeUnit.SECONDS);
			return true;
		}
		else {
//			System.out.println("아직남아있음");
			return false;
		}
	}
	
//	public Boolean existValue(String key) {
//		ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
//		stringValueOperations
//	}
}
