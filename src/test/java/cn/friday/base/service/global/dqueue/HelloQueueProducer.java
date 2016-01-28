package cn.friday.base.service.global.dqueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.support.queue.AbstractQueueProducer;

@Component("helloQueueProducer")
public class HelloQueueProducer extends AbstractQueueProducer<String> {

	@Autowired
	RedisConnectionFactory messageRedisFactory;
	
	public HelloQueueProducer() {
		super("hello-2",String.class);
	}

	@Override
	public RedisConnectionFactory redisConntion() {
		return messageRedisFactory;
	}

}
