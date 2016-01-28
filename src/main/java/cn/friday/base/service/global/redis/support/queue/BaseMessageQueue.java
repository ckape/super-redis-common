package cn.friday.base.service.global.redis.support.queue;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;

public abstract class BaseMessageQueue<T> {

	private final static Logger logger = Logger.getLogger(AbstractQueueConsumer.class);
	
	private final static String REDIS_QUEUE_D_SUFFIX =".Queue.D";
	
	//消息通道
	private String channel;
	
	private Class<T> entityClazz;
	
	
	public BaseMessageQueue(String baseChannel, Class<T> entityClazz){
		this.entityClazz = entityClazz;
		createChannel(baseChannel);
	}
	
	private void createChannel(String baseChannel){
		if( !Strings.isNullOrEmpty(baseChannel)){
			channel =  new StringBuilder().append(baseChannel).append(entityClazz.getSimpleName()).append(REDIS_QUEUE_D_SUFFIX).toString();
		}else{
			channel =  new StringBuilder().append(entityClazz.getSimpleName()).append(REDIS_QUEUE_D_SUFFIX).toString();
		}
		logger.info(" Queue channel:"+channel);
	}
	
	/**
	 * 获取redis查找的句柄
	 * @return
	 *@author BravoZu
	 */
    StringRedisTemplate template(){
		if( redisConntion() != null){
			return new StringRedisTemplate(redisConntion());
		}
		return null;
	}
		
	public abstract RedisConnectionFactory redisConntion();
	
	/**
	 * 获取消息通道
	 * @return
	 *@author BravoZu
	 */
	public String getChannel(){
		return channel;
	}
	
	/**
	 * 对象转换成字符串
	 * @param t
	 * @return
	 *@author BravoZu
	 */
	 String objectToString(T t){
		if( checkBaseDataType(entityClazz)){
			return String.valueOf(t);
		}else{
			return JSON.toJSONString(t);
		}
	}
	
	/**
	 * 字符串转换成对象
	 * @param str
	 * @return
	 *@author BravoZu
	 */
	@SuppressWarnings({ "unchecked" })
	T stringToObject(String str){
		if( checkBaseDataType(entityClazz) ){
			return (T) getBaseResult(str, entityClazz);
		}else{
			return JSON.parseObject(str, entityClazz);
		}
	}
	
	private Object getBaseResult(String member, Class<T> c ){
		String className = c.getName();
		if("java.lang.Integer".equals(className)){
			return Integer.parseInt(member);
		}else if("java.lang.Long".equals(className)){
			return Long.parseLong(member);
		}else if("java.lang.Boolean".equals(className)){
			return Boolean .parseBoolean(member);
		}else if("java.lang.String".equals(className)){
			return member;
		}
		return member;
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
}
