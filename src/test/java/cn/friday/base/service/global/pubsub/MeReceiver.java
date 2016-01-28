package cn.friday.base.service.global.pubsub;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class MeReceiver implements MessageListener{
	
	private static final Logger logger = Logger.getLogger(MeReceiver.class);
	
	RedisSerializer<String> serializer = new StringRedisSerializer();
	
	public void reviceMessage(String message){
		logger.info("revice < "+message+" >");
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = new String(pattern);
		if( channel.equals("chat")){
			reviceMessage( serializer.deserialize(message.getBody()) +":"+message.getChannel().toString());
		}
	}

}
