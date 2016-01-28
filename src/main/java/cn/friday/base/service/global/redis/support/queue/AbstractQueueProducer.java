package cn.friday.base.service.global.redis.support.queue;

import org.apache.log4j.Logger;

/**
 * 队列消息生产者
 * @author BravoZu
 *
 */
public abstract class AbstractQueueProducer<T> extends BaseMessageQueue<T> {
	
	Logger logger = Logger.getLogger(AbstractQueueProducer.class);
	
	public AbstractQueueProducer( Class<T> entityClazz) {
		this(null, entityClazz);
	}

	public AbstractQueueProducer(String baseChannel, Class<T> entityClazz) {
		super(baseChannel, entityClazz);
	}

	/**
	 * 将消息放入队列中
	 * @param t
	 *@author BravoZu
	 */
	public void push(T t){
		template().boundListOps(getChannel()).leftPush(objectToString(t));
	}
	
	
}
