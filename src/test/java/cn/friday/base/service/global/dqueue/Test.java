package cn.friday.base.service.global.dqueue;

import java.util.concurrent.CountDownLatch;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Test {

	@Autowired
	CountDownLatch latch;
	
	@Autowired
	HelloQueueConsumer helloQueueConsumer;
	
	@Autowired
	HelloQueueProducer helloQueueProducer;
	
	@org.junit.Test
	public void testConsumer(){
		System.out.println( helloQueueConsumer.getChannel() );
	}
	
	@org.junit.Test
	public void testProducer(){
		try {
			Thread.sleep(10*60*1000);
			for( int i=0;i<100;i++){
				helloQueueProducer.push("hello word");
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
