package cn.friday.base.service.global.r.mapper;

import org.springframework.stereotype.Component;

import cn.friday.base.service.global.r.model.TreeholeMessageRedis;
import cn.friday.base.service.global.redis.mapper.BaseRedisMapper;
@Component("treeholeMessageRedisMapper")
public class TreeholeMessageRedisMapper extends BaseRedisMapper<TreeholeMessageRedis> {

	public TreeholeMessageRedisMapper() {
		super(TreeholeMessageRedis.class);
		// TODO Auto-generated constructor stub
	}

}
