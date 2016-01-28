package cn.friday.base.service.global.pubsub;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.friday.base.service.global.redis.support.pubsub.AbstractMessageReceiver;

@Service("intReceiver")
public class IntReceiver extends AbstractMessageReceiver<Integer>{
	
	private static final Logger logger = Logger.getLogger(IntReceiver.class);

	public IntReceiver() {
		super("hello-1", Integer.class);
	}

	@Override
	public void receive(Integer t) {
		logger.info(" revInt:<"+t+">");
	}
	


}
