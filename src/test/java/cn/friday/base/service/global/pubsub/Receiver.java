package cn.friday.base.service.global.pubsub;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.friday.base.service.global.redis.support.pubsub.AbstractMessageReceiver;

@Service("receiver")
public class Receiver extends AbstractMessageReceiver<String>{
	
	private static final Logger logger = Logger.getLogger(Receiver.class);

	public Receiver() {
		super("hello-1", String.class);
	}


	@Override
	public void receive(String t) {
		logger.info(" revStr:<"+t+">");
	}
	


}
