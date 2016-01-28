package cn.friday.base.service.global.queue.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.queue.QueueTask;
import cn.friday.base.service.global.queue.QueueTaskV2;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class QueueTaskTest {

	@Autowired
	QueueTask queueTask;
	@Autowired
	QueueTaskV2 queueTaskV2;
	
	
	@Test
	public void test() throws InterruptedException{
		queueTask.push("hello queue");
//		Thread.sleep(1000);
		queueTask.push("hello queue1");
//		Thread.sleep(1000);
		queueTask.push("hello queue2");
//		Thread.sleep(1000);
		queueTask.push("hello queue3");
		queueTaskV2.push("word.......");
		Thread.sleep(1000);
	}
}
