package cn.friday.base.service.global.pubsub;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.friday.base.service.global.redis.support.pubsub.AbstractMessageReceiver;

@Service("intBakReceiver")
public class IntBakReceiver extends AbstractMessageReceiver<Integer>{
	
	private static final Logger logger = Logger.getLogger(IntBakReceiver.class);

	public IntBakReceiver() {
		super("hello-1", Integer.class);
	}

	@Override
	public void receive(Integer t) {
		logger.info(" revInt:<"+t+">");
	}
	


}
