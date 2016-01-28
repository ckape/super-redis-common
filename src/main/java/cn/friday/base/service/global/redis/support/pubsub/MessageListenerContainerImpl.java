package cn.friday.base.service.global.redis.support.pubsub;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


/**
 * 发布者，订阅者容器
 * @author BravoZu
 *
 */
public class MessageListenerContainerImpl extends RedisMessageListenerContainer{

	public static RedisConnectionFactory connection = null;
	
	public MessageListenerContainerImpl(RedisConnectionFactory jedisConnectionFactory){
		connection = jedisConnectionFactory;
		setConnectionFactory(jedisConnectionFactory);
	}
	
	public void addMessageListener(AbstractMessageReceiver<?> receiver) {
		super.addMessageListener(receiver, new PatternTopic(receiver.getChannel()));
	}
	
}
