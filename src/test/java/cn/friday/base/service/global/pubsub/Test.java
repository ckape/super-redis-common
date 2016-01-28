package cn.friday.base.service.global.pubsub;

import java.util.concurrent.CountDownLatch;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Test {

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	CountDownLatch latch;
	
	@Autowired
	Receiver receiver;
	@Autowired
	IntReceiver intReceiver;
	
	@Autowired
	IntSender intSender;
	
	@org.junit.Test
	public void testChat(){
		
		stringRedisTemplate.convertAndSend(receiver.getChannel(), "hello pubsub");
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" close");
	}
	
	@org.junit.Test
	public void testIntChat(){
		for( int i = 0; i< 10;i++){
			intSender.send(i);
//			stringRedisTemplate.convertAndSend(intReceiver.getChannel(), String.valueOf(i));
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" close");
	}
	
	@org.junit.Test
	public void testRev(){
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" close");
	}
	
}
