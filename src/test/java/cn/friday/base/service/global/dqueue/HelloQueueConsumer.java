package cn.friday.base.service.global.dqueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.support.queue.AbstractQueueConsumer;

@Component("helloQueueConsumer")
public class HelloQueueConsumer extends AbstractQueueConsumer<String> {

	Logger logger = Logger.getLogger(HelloQueueConsumer.class);
	
	@Autowired
	RedisConnectionFactory messageRedisFactory;
	
	public HelloQueueConsumer() {
		super("hello-2",String.class);
	}

	@Override
	protected void onMessage(String t) {
		logger.info("rev < "+t+" >");
	}

	@Override
	public RedisConnectionFactory redisConntion() {
		return messageRedisFactory;
	}

}
