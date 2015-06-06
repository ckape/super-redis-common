package cn.friday.base.service.global.segment.dao.impl;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.dao.impl.BaseZsetSegmentRedisDaoImpl;
import cn.friday.base.service.global.segment.dao.ISegmentTimelineDao;
@Component
public class SegmentTimelineDaoImpl extends BaseZsetSegmentRedisDaoImpl implements ISegmentTimelineDao {
	
	private final static String BASE_KEY ="SegmentTimeline:schoolId";
	
	@Resource(name="stringRedisTemplate1")
	private StringRedisTemplate stringRedisTemplate1;
	
	private final static int SEGMENTSIZE = 3;

	public SegmentTimelineDaoImpl() {
		super(BASE_KEY, SEGMENTSIZE);
		
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate1;
	}

}
