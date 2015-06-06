package cn.friday.base.service.global.redis.dao.zset;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.segment.dao.ISegmentTimelineDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class BaseZsetSegmentRedisDaoTests {
	
	@Autowired
	ISegmentTimelineDao segmentTimelineDao;
	
	@Test
	public void testSize(){
		print(segmentTimelineDao.size(1));
	}
	
	@Test
	public void testRangeSize(){
		print(segmentTimelineDao.rangeSize(150,180, 1));
	}
	@Test
	public void testRemove(){
		print(segmentTimelineDao.remove("77", 1));
	}
	@Test
	public void testExistMember(){
		print(segmentTimelineDao.existMember("67", 1));
	}
	
	@Test
	public void testGetScore(){
		print(segmentTimelineDao.getScore("67", 1));
	}
	
	@Test
	public void testFindByScoreDesc(){
		print(segmentTimelineDao.findByScoreDesc(100, 120, 1));
	}
	@Test
	public void testFindByScoreDesc2(){
		print(segmentTimelineDao.findByScoreDesc(0, 200, 1));
		print(segmentTimelineDao.findByScoreDesc(0, 200, 0L, 20, 1));
	}
	
	@Test
	public void testFindByIdDesc(){
		print(segmentTimelineDao.findByIdDesc(0, 100, 1));
	}
	@Test
	public void testAddElement(){
		for(int i=0;i<100;i++){
			segmentTimelineDao.add(String.valueOf(i), i+100, 1);
		}
	}
	
	@Test
	public void testAddOneElement(){
		segmentTimelineDao.add("91", 191, 1);
	}
	
	public void testAddMoreElement(){
		Set<TypedTuple<String>> tuples = new HashSet<TypedTuple<String>>();
		tuples.add(new DefaultTypedTuple<String>("1", (double)System.currentTimeMillis()));
		segmentTimelineDao.add(tuples, 1);
	}
	
	
	private void print(Object o){
		System.out.println(o);
	}

}
