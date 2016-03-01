package cn.friday.base.service.global.redis.support.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import cn.friday.base.service.global.redis.util.FibonacciUtil;

import com.google.common.base.Strings;

/**
 * 抽象对象消费者
 * @author BravoZu
 *
 */
public abstract class AbstractQueueConsumer<T> extends BaseMessageQueue<T> {

	Logger logger = Logger.getLogger(AbstractQueueConsumer.class);
	
	private  ExecutorService executorService = null;
	
	public AbstractQueueConsumer( Class<T> entityClazz) {
		this(null, entityClazz);
	}
	
	public AbstractQueueConsumer(String baseChannel, Class<T> entityClazz) {
		super(baseChannel, entityClazz);
		if(executorService == null){
			executorService = Executors.newFixedThreadPool(2);
			executorService.execute(new TaskExecutor() );
			logger.info(" start QueueConsumer executor...");
		}
	}
	
	class TaskExecutor implements Runnable{
		@Override
		public void run() {
			int fiboN = 0;
			while(true){
				T t = pop();
				if( t != null ){
					fiboN = 0;
					try {
						onMessage(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					try {
						long sleepSecond = FibonacciUtil.fibonacciNormal(fiboN);
						if( sleepSecond > 60){
							fiboN = 0;
							sleepSecond = FibonacciUtil.fibonacciNormal(fiboN);
						}else{
							fiboN++;
						}
						Thread.sleep(sleepSecond*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 消息出队列
	 * @return
	 *@author BravoZu
	 */
	private T pop(){
		if( template() == null){
			return null;
		}
		
		String key = getChannel();
		String retVal = template().boundListOps(key).rightPop();
		if( !Strings.isNullOrEmpty(retVal) ){
			return stringToObject(retVal);
		}
		return null;
	}

	/**
	 * 消息的处理方法
	 * @param t
	 *@author BravoZu
	 */
	protected abstract void onMessage(T t) throws Exception;
	
	
	
	
	
	
}
