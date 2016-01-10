package cn.friday.base.service.global.zset.test;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.zset.dao.ITestTimelineDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestTimelineDaoTests {
	
	@Autowired
	ITestTimelineDao testTimelineDao;
	
	@Test
	public void testAdd(){
		int [] i = new int[0];
		testTimelineDao.add(100L, 1001d,i );
	}
	
	@Test
	public void testfind(){
		int [] i = new int[0];
		Set<Long> members = testTimelineDao.findByIdDesc(0, 100, i);
		System.out.println(members);
	}

}
