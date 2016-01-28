package cn.friday.base.service.global.redis.support.pubsub;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;

/**
 * 抽象的消息订阅者
 * @author BravoZu
 *
 * @param <T>
 */
public abstract class AbstractMessageReceiver<T> implements MessageListener {
	
	private static final Logger logger = Logger.getLogger(AbstractMessageReceiver.class);

	private final static String REDIS_PUBSUB_SUFFIX =".Pubsub";
	
	private Class<T> entityClazz;
	
	//消息通道
	private String channel;
	
	RedisSerializer<String> serializer = new StringRedisSerializer();
	
	/**
	 * 提供订阅的前缀
	 * @param baseChannel
	 * @param entityClazz
	 */
	public AbstractMessageReceiver(String baseChannel, Class<T> entityClazz){
		this.entityClazz = entityClazz;
		createChannel(baseChannel);
	}
	
	public AbstractMessageReceiver(Class<T> entityClazz){
		this(null, entityClazz);
	}
	
	private void createChannel(String baseChannel){
		if( !Strings.isNullOrEmpty(baseChannel)){
			channel =  new StringBuilder().append(baseChannel).append(entityClazz.getSimpleName()).append(REDIS_PUBSUB_SUFFIX).toString();
		}else{
			channel =  new StringBuilder().append(entityClazz.getSimpleName()).append(REDIS_PUBSUB_SUFFIX).toString();
		}
		logger.info(" pubsub channel:"+channel);
	}
	
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String transportChannel = new String(pattern);
		if( channel.equals(transportChannel) ){
			String receiverStr = serializer.deserialize(message.getBody());
			String mChannel =  serializer.deserialize(message.getChannel());
			//转换成对应的对象
			if( channel.equals(mChannel) && !Strings.isNullOrEmpty(receiverStr)){
				T t = stringToObject(receiverStr);
				receive(t);
			}
		}
	}
	
	/**
	 * 处理接收到的消息
	 * @param t
	 *@author BravoZu
	 */
	public abstract void receive(T t);
	
	/**
	 * 获取消息通道
	 * @return
	 *@author BravoZu
	 */
	public String getChannel(){
		return channel;
	}
	
	
	/**
	 * 字符串转换成对象
	 * @param str
	 * @return
	 *@author BravoZu
	 */
	@SuppressWarnings("unchecked")
	private T stringToObject(String str){
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
