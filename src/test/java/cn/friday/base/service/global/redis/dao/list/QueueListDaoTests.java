package cn.friday.base.service.global.redis.dao.list;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.list.dao.IQueueListDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class QueueListDaoTests {
	@Autowired
	IQueueListDao queueListDao;
	
	@Test
	public void lpush(){
		for(int i=0;i<20;i++){
			queueListDao.lpush(String.valueOf(i), 1);
		}
	}
	
	@Test
	public void rpop(){
		for(int i=0;i<20;i++){
			System.out.println(queueListDao.rpop(1));
		}
	}

}
