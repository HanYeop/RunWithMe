package com.ssafy.gumid101.redis;

public interface RedisService {

	Boolean getRedisStringValue(String key, Integer delayTime);

}
