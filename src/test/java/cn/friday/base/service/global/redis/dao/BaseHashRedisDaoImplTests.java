package cn.friday.base.service.global.redis.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.r.ITreeholeMessageRedisDao;
import cn.friday.base.service.global.r.mapper.TreeholeMessageRedisMapper;
import cn.friday.base.service.global.r.model.TreeholeMessageRedis;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class BaseHashRedisDaoImplTests {
	
	@Autowired
	ITreeholeMessageRedisDao treeholeMessageRedisDao;
	@Autowired
	TreeholeMessageRedisMapper treeholeMessageRedisMapper;
	
	@Test
	public void mitlFind(){
		List<Long> list = new ArrayList<Long>();
		list.add(13091L);
//		list.add(13108L);
//		list.add(11330L);
		List<TreeholeMessageRedis> t = treeholeMessageRedisDao.multiFindByIds(list);
		
//		TreeholeMessageRedis t = treeholeMessageRedisDao.findById(13091L);
//		System.out.println(t);
	}

}
