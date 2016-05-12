package cn.friday.base.service.global.goods.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.goods.dao.GoodsRedisDao;
import cn.friday.base.service.global.goods.mapper.GoodsRedisMapper;
import cn.friday.base.service.global.goods.model.GoodsRedis;
import cn.friday.base.service.global.redis.dao.impl.BaseHashRedisDaoImpl;
import cn.friday.base.service.global.redis.mapper.IBaseRedisMapper;

@Component("goodsRedisDao")
public class GoodsRedisDaoImpl extends BaseHashRedisDaoImpl<GoodsRedis> implements GoodsRedisDao {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	GoodsRedisMapper mapper;

	public GoodsRedisDaoImpl() {
		super(GoodsRedis.class, true);
	}

	@Override
	public IBaseRedisMapper<GoodsRedis> getBaseRedisMapper() {
		return mapper;
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate;
	}

	@Override
	public int getCacheTime() {
		return 5;
	}

}
