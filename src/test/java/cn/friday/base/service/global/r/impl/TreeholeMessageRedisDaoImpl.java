package cn.friday.base.service.global.r.impl;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.r.ITreeholeMessageRedisDao;
import cn.friday.base.service.global.r.mapper.TreeholeMessageRedisMapper;
import cn.friday.base.service.global.r.model.TreeholeMessageRedis;
import cn.friday.base.service.global.redis.dao.impl.BaseHashRedisDaoImpl;
import cn.friday.base.service.global.redis.mapper.IBaseRedisMapper;

@Component("treeholeMessageRedisDao")
public class TreeholeMessageRedisDaoImpl extends BaseHashRedisDaoImpl<TreeholeMessageRedis> implements ITreeholeMessageRedisDao {
	
	@Resource
	TreeholeMessageRedisMapper treeholeMessageRedisMapper;
	
	@Resource(name="stringRedisTemplate1")
	private StringRedisTemplate stringRedisTemplate1;

	public TreeholeMessageRedisDaoImpl() {
		super(TreeholeMessageRedis.class);
	}

	@Override
	public IBaseRedisMapper<TreeholeMessageRedis> getBaseRedisMapper() {
		return treeholeMessageRedisMapper;
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate1;
	}

}
