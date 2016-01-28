package cn.friday.base.service.global.redis.support.pubsub;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;

/**
 * 抽象的消息发送者
 * @author BravoZu
 *
 * @param <T>
 */
public abstract class AbstractMessageSender<T> {
	
	private static final Logger logger = Logger.getLogger(AbstractMessageSender.class);
	
	private Class<T> entityClazz;
	
	//消息通道
	private String channel;
	
	private final static String REDIS_PUBSUB_SUFFIX =".Pubsub";
	
	public AbstractMessageSender(String baseChannel, Class<T> entityClazz){
		this.entityClazz = entityClazz;
		createChannel(baseChannel);
	}
	
	public AbstractMessageSender(Class<T> entityClazz){
		this(null, entityClazz);
	}
	

		
	private void send(String message){
		template().convertAndSend(channel, message);
	}
	
	public void send(T t){
		send(objectToString(t));
	}
	
	/**
	 * 对象转换成字符串
	 * @param t
	 * @return
	 *@author BravoZu
	 */
	private String objectToString(T t){
		if( checkBaseDataType(entityClazz)){
			return String.valueOf(t);
		}else{
			return JSON.toJSONString(t);
		}
	}
	
	/**
	 * 检查基本数据类型
	 * @param c
	 * @return
	 */
	private boolean checkBaseDataType(Class<T> c){
		String className = c.getName();
		if("java.lang.Integer".equals(className) ||
				"java.lang.Long".equals(className) ||
				"java.lang.Boolean".equals(className) ||
				"java.lang.Character".equals(className) ||
				"java.lang.Byte".equals(className) ||
				"java.lang.Short".equals(className) ||
				"java.lang.Float".equals(className) ||
				"java.lang.Double".equals(className) ||
				"java.lang.String".equals(className) ){
			return true;
		}
		return false;
	}
	
	
	private StringRedisTemplate template(){
		return new StringRedisTemplate(redisConntion());
	}
	
	private void createChannel(String baseChannel){
		if( !Strings.isNullOrEmpty(baseChannel)){
			channel =  new StringBuilder().append(baseChannel).append(entityClazz.getSimpleName()).append(REDIS_PUBSUB_SUFFIX).toString();
		}else{
			channel =  new StringBuilder().append(entityClazz.getSimpleName()).append(REDIS_PUBSUB_SUFFIX).toString();
		}
		logger.info(" pubsub channel:"+channel);
	}
	
	
	public abstract RedisConnectionFactory redisConntion();
	

}
