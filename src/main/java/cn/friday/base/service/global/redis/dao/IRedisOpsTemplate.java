package cn.friday.base.service.global.redis.dao;

import org.springframework.data.redis.core.StringRedisTemplate;

public interface IRedisOpsTemplate {
	
	StringRedisTemplate stringRedisTemplate();

}
