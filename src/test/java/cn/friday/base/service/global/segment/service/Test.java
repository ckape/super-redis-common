package cn.friday.base.service.global.segment.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.friday.base.service.global.Application;
import cn.friday.base.service.global.segment.dao.ISegmentTimelineDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class Test {
	
	@Autowired
	ISegmentTimelineDao segmentTimelineDao;
	

	@org.junit.Test
	public void testSegmentFindByScoreWithScoresAsc(){
		segmentTimelineDao.findByScoreWithScoresAsc(0, System.currentTimeMillis(), 12);
	}
}
