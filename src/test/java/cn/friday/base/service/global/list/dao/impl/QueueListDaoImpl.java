package cn.friday.base.service.global.list.dao.impl;

import org.springframework.stereotype.Component;

import cn.friday.base.service.global.list.dao.IQueueListDao;
import cn.friday.base.service.global.redis.dao.impl.BaseListRedisDaoImpl;

@Component("queueListDao")
public class QueueListDaoImpl extends BaseListRedisDaoImpl implements IQueueListDao {

	public final static String baseKey = "Queue:test:id";
	
	public QueueListDaoImpl() {
		super(baseKey);
	}

}
