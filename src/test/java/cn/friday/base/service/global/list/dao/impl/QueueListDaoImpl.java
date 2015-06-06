package cn.friday.base.service.global.list.dao.impl;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.friday.base.service.global.list.dao.IQueueListDao;
import cn.friday.base.service.global.redis.dao.impl.BaseListRedisDaoImpl;

@Component("queueListDao")
public class QueueListDaoImpl extends BaseListRedisDaoImpl implements IQueueListDao {

	private final static String baseKey = "Queue:test:id";
	
	@Resource(name="stringRedisTemplate1")
	private StringRedisTemplate stringRedisTemplate1;
	
	public QueueListDaoImpl() {
		super(baseKey);
	}

	@Override
	public StringRedisTemplate stringRedisTemplate() {
		return stringRedisTemplate1;
	}

}
