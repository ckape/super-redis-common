package cn.friday.base.service.global.segment.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.redis.dao.impl.BaseZsetSegmentRedisDaoImpl;
import cn.friday.base.service.global.segment.dao.ISegmentTimelineDao;
import cn.friday.base.service.global.segment.data.MemberData;

@Component("segmentTimelineDao")
public class SegmentTimelineDaoImpl extends BaseZsetSegmentRedisDaoImpl<MemberData> implements ISegmentTimelineDao {

	private final static String BASE_KEY = "MatchUserLikeTimeline:province";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private final static int SEGMENTSIZE = 2;

	public SegmentTimelineDaoImpl() {
		super(BASE_KEY, SEGMENTSIZE, MemberData.class);
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate;
	}

}
