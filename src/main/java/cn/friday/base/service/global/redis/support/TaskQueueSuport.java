package cn.friday.base.service.global.redis.support;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import cn.friday.base.service.global.redis.dao.IRedisOpsTemplate;
import cn.friday.base.service.global.redis.registry.RegistryService;
import cn.friday.base.service.global.redis.util.FibonacciUtil;

/**
 * 基于redis封装的消息队列
 * @author BravoZu
 *
 * @param <T>
 */
public abstract class TaskQueueSuport<T> implements IRedisOpsTemplate {

	Logger logger = Logger.getLogger(TaskQueueSuport.class);

	private Class<T> entityClazz;

	private String baseKey;

	//保存检查队列是否有重复消息的key
	private String checkKey;

	/**
	 * 默认的队列后缀
	 */
	private final static String REDIS_QUEUE_SUFFIX = ".Queue";

	/**
	 * 校验队列的后缀
	 */
	private final static String REDIS_CHECK_QUEUE_SUFFIX = ".Timeline.Queue";

	private volatile Thread taskThread;

	private Object lock = new Object();

	public TaskQueueSuport(Class<T> entityClazz) {
		this("", entityClazz);
	}

	public TaskQueueSuport(String baseKey, Class<T> entityClazz) {
		this.baseKey = baseKey;
		this.entityClazz = entityClazz;
		createKey();
		initTaskThread().start();
		;
	}

	/**
	 * 初始化任务线程
	 * @return 
	 */
	private Thread initTaskThread() {
		if (taskThread == null) {
			synchronized (lock) {
				if (taskThread != null) {
					taskThread = new Thread(new TaskExecutor());
					taskThread.setDaemon(true);
				}
			}
		}
		return taskThread;
	}

	/**
	 * 检查并重启线程
	 * 线程挂掉后，重启该任务线程
	 */
	private void checkAndRestartTaskThread() {
		if (!taskThread.isAlive()) {
			synchronized (lock) {
				if (!taskThread.isAlive()) {
					taskThread = new Thread(new TaskExecutor());
					taskThread.start();
				}
			}
		}
	}

	/**
	 * 进入任务队列
	 * @param t
	 *@author BravoZu
	 */
	public void push(T t) {
		String msg = objectToString(t);
		push(msg);
	}

	/**
	 * 相同消息，同一时刻内只有一个
	 * 当这条消息被消费了
	 * 才会再次进入任务队列
	 * @param t
	 *@author BravoZu
	 */
	public void singlePush(T t) {

		String msg = objectToString(t);
		String checkKey = buildCheckKey();
		Double score = stringRedisTemplate().boundZSetOps(checkKey).score(msg);
		if (score == null) {
			//检查队列中是否存在该消息
			push(msg);
			stringRedisTemplate().boundZSetOps(checkKey).add(msg, System.currentTimeMillis());
		}
	}

	private void push(String msg) {
		String key = buildKey();
		stringRedisTemplate().boundListOps(key).leftPush(msg);
		checkAndRestartTaskThread();
	}

	private T pop() {

		if (stringRedisTemplate() == null) {
			return null;
		}
		String key = buildKey();
		String retVal = stringRedisTemplate().boundListOps(key).rightPop();
		if (!Strings.isNullOrEmpty(retVal)) {
			//从监控队列中去掉
			String checkKey = buildCheckKey();
			stringRedisTemplate().boundZSetOps(checkKey).remove(retVal);
			return stringToObject(retVal);
		}
		return null;
	}

	private void createKey() {
		String keyName = createKeyName();
		this.baseKey = new StringBuffer().append(baseKey).append(keyName).append(REDIS_QUEUE_SUFFIX).append(":{0}")
				.toString();
		this.checkKey = new StringBuffer().append(baseKey).append(keyName).append(REDIS_CHECK_QUEUE_SUFFIX)
				.append(":{0}").toString();
		RegistryService.registry(baseKey);
		RegistryService.registry(checkKey);
	}

	private String createKeyName() {
		String entityClazzName = entityClazz.getSimpleName();
		return entityClazzName;
	}

	/**
	 * 构造key
	 * @param ids
	 * @return
	 * @author BravoZu
	 */
	private String buildKey(long... ids) {
		return baseBuildKey(baseKey, ids);
	}

	/**
	 * 
	 * @param ids
	 * @return
	 * @author BravoZu
	 */
	private String buildCheckKey(long... ids) {
		return baseBuildKey(checkKey, ids);
	}

	private String baseBuildKey(String bk, long... ids) {
		if (ids.length == 0) {
			return bk.replace(":{0}", "");
		} else {
			StringBuilder builder = new StringBuilder();
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}
			Joiner.on(":").appendTo(builder, list);
			return MessageFormat.format(bk, builder.toString());
		}
	}

	/**
	 * 对象转换成字符串
	 * @param t
	 * @return
	 *@author BravoZu
	 */
	private String objectToString(T t) {
		if (checkBaseDataType(entityClazz)) {
			return String.valueOf(t);
		} else {
			return JSON.toJSONString(t);
		}
	}

	/**
	 * 字符串转换成对象
	 * @param str
	 * @return
	 * @author BravoZu
	 */
	@SuppressWarnings("unchecked")
	private T stringToObject(String str) {
		if (checkBaseDataType(entityClazz)) {
			return (T) getBaseResult(str, entityClazz);
		} else {
			return JSON.parseObject(str, entityClazz);
		}
	}

	private Object getBaseResult(String member, Class<T> c) {
		String className = c.getName();
		if ("java.lang.Integer".equals(className)) {
			return Integer.parseInt(member);
		} else if ("java.lang.Long".equals(className)) {
			return Long.parseLong(member);
		} else if ("java.lang.Boolean".equals(className)) {
			return Boolean.parseBoolean(member);
		} else if ("java.lang.String".equals(className)) {
			return member;
		}
		return member;
	}

	/**
	 * 检查基本数据类型
	 * @param c
	 * @return
	 */
	private boolean checkBaseDataType(Class<T> c) {
		String className = c.getName();
		if ("java.lang.Integer".equals(className) || "java.lang.Long".equals(className)
				|| "java.lang.Boolean".equals(className) || "java.lang.Character".equals(className)
				|| "java.lang.Byte".equals(className) || "java.lang.Short".equals(className)
				|| "java.lang.Float".equals(className) || "java.lang.Double".equals(className)
				|| "java.lang.String".equals(className)) {
			return true;
		}
		return false;
	}

	/**
	 * 消息的处理方法
	 * @param t
	 *@author BravoZu
	 */
	protected abstract void onMessage(T t) throws Exception;

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
						if (sleepSecond > 10) {
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

}
