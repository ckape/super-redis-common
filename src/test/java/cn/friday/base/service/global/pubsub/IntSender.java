package cn.friday.base.service.global.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.support.pubsub.AbstractMessageSender;

@Component("intSender")
public class IntSender extends AbstractMessageSender<Integer> {

	@Autowired
	RedisConnectionFactory messageRedisFactory;
	
	public IntSender() {
		super("hello-1",Integer.class);
	}
	
	@Override
	public RedisConnectionFactory redisConntion() {
		return messageRedisFactory;
	}

}
