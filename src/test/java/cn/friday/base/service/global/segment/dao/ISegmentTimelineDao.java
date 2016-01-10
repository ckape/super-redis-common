package cn.friday.base.service.global.segment.dao;

import cn.friday.base.service.global.redis.dao.IBaseZsetRedisDao;
import cn.friday.base.service.global.segment.data.MemberData;

public interface ISegmentTimelineDao extends IBaseZsetRedisDao<MemberData> {

}
