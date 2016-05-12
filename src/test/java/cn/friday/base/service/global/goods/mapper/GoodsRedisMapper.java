package cn.friday.base.service.global.goods.mapper;

import org.springframework.stereotype.Component;

import cn.friday.base.service.global.goods.model.GoodsRedis;
import cn.friday.base.service.global.redis.mapper.BaseRedisMapper;

@Component("goodsRedisMapper")
public class GoodsRedisMapper extends BaseRedisMapper<GoodsRedis> {

	public GoodsRedisMapper() {
		super(GoodsRedis.class);
	}

}
