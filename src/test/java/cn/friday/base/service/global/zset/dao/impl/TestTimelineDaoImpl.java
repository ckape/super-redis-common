package cn.friday.base.service.global.zset.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.dao.impl.BaseZsetRedisDaoImpl;
import cn.friday.base.service.global.zset.dao.ITestTimelineDao;

@Component("testTimelineDao")
public class TestTimelineDaoImpl extends BaseZsetRedisDaoImpl<Long> implements
		ITestTimelineDao {
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	public static final String baseKey = "TestTimeline";
	public TestTimelineDaoImpl() {
		super(baseKey, Long.class);
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate;
	}

	
}
