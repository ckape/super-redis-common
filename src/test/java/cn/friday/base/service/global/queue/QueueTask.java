package cn.friday.base.service.global.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.support.TaskQueueSuport;

@Component("queueTask")
public class QueueTask extends TaskQueueSuport<String> {

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	public QueueTask() {
		super(String.class);
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate;
	}

	@Override
	protected void onMessage(String t) {
		System.out.println("rev message:"+t);
	}

}
