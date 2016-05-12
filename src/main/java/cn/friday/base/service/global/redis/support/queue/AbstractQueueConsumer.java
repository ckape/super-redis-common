package cn.friday.base.service.global.redis.support.queue;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import cn.friday.base.service.global.redis.util.FibonacciUtil;

/**
 * 抽象对象消费者
 * @author BravoZu
 *
 */
public abstract class AbstractQueueConsumer<T> extends BaseMessageQueue<T> {

	Logger logger = Logger.getLogger(AbstractQueueConsumer.class);

	private volatile Thread taskThread;

	private Object lock = new Object();

	public AbstractQueueConsumer(Class<T> entityClazz) {
		this(null, entityClazz);
	}

	public AbstractQueueConsumer(String baseChannel, Class<T> entityClazz) {
		super(baseChannel, entityClazz);
		initTaskThread().start();
	}

	/**
	 * 初始化任务线程
	 * @return 
	 */
	private Thread initTaskThread() {
		if (taskThread == null) {
			synchronized (lock) {
				if (taskThread == null) {
					taskThread = new Thread(new TaskExecutor());
					taskThread.setDaemon(true);
				}
			}
		}
		return taskThread;
	}

	class TaskExecutor implements Runnable {
		@Override
		public void run() {
			int fiboN = 0;
			while (true) {
				T t = pop();
				if (t != null) {
					fiboN = 0;
					try {
						onMessage(t);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						long sleepSecond = FibonacciUtil.fibonacciNormal(fiboN);
						if (sleepSecond > 60) {
							fiboN = 0;
							sleepSecond = FibonacciUtil.fibonacciNormal(fiboN);
						} else {
							fiboN++;
						}
						TimeUnit.SECONDS.sleep(sleepSecond);
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
	private T pop() {
		if (template() == null) {
			return null;
		}

		String key = getChannel();
		String retVal = template().boundListOps(key).rightPop();
		if (!Strings.isNullOrEmpty(retVal)) {
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
