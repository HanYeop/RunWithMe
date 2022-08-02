package com.ssafy.gumid101.redis;

public interface RedisService {

	Boolean getIsUseable(String key, Integer delayTime) throws Exception;

}
