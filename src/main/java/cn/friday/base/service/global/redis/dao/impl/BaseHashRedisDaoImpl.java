package cn.friday.base.service.global.redis.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import cn.friday.base.service.global.redis.dao.IBaseHashRedisDao;
import cn.friday.base.service.global.redis.dao.IRedisOpsTemplate;
import cn.friday.base.service.global.redis.loader.LoaderResult;
import cn.friday.base.service.global.redis.loader.RedisLoader;
import cn.friday.base.service.global.redis.registry.RegistryService;
import cn.friday.base.service.global.redis.support.cache.AbstractLocalCache;
import cn.friday.base.service.global.redis.syncer.Syncer;
import cn.friday.base.service.global.redis.util.Constant;
import cn.friday.base.service.global.redis.util.MethodHelper;
import cn.friday.base.service.global.redis.util.ReflectUtil;

public abstract class BaseHashRedisDaoImpl<T> implements IBaseHashRedisDao<T>, IRedisOpsTemplate {

	private Class<T> entityClazz;

	private String baseKey;

	//是否本地缓存
	private boolean isLocalCache;

	/**
	 * 默认缓存的时间,1s
	 */
	private final static int DEFAULT_CAHCE_SECOND = 1;

	//本地缓存时间
	private int cacheTime;

	private volatile EntityLocalCache cache;

	//对象锁
	private Object lock = new Object();

	private final ListeningExecutorService executorService;

	public BaseHashRedisDaoImpl(Class<T> entityClazz) {
		this(entityClazz, false);
	}

	public BaseHashRedisDaoImpl(Class<T> entityClazz, boolean isLocalCache) {
		this.isLocalCache = isLocalCache;
		this.entityClazz = entityClazz;
		buildKey();
		executorService = MoreExecutors
				.listeningDecorator(MoreExecutors.getExitingExecutorService(new ThreadPoolExecutor(1,
						MethodHelper.defaultMaxThreads(), 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>())));
	}

	@Override
	public T findById(long id) {
		if (isLocalCache) {
			return obtainByCache(id);
		} else {
			return doGetById(id);
		}
	}

	/**
	 * 直接从redis获取
	 * @param id
	 * @return 
	 */
	private T doGetById(long id) {
		String key = MessageFormat.format(baseKey, id + "");
		Map<Object, Object> entityMap = stringRedisTemplate().opsForHash().entries(key);
		entityMap.put("id", id);
		return getBaseRedisMapper().fromObjectHash(entityMap);
	}

	/**
	 * 
	 * @param id
	 * @param loader (当redis中不存在时，如何恢复，继承这个抽象类)
	 */
	@Override
	public T findById(long id, RedisLoader<T> loader) {
		T t = null;
		if (isLocalCache) {
			t = obtainByCache(id);
		}

		if (t == null) {
			t = findByIdToRedis(id, loader);
		}
		return t;
	}

	private T findByIdToRedis(long id, RedisLoader<T> loader) {
		T t = doGetById(id);
		if (t == null) {
			synchronized (lock) {
				//双重检查锁
				t = doGetById(id);
				if (t == null) {
					LoaderResult<T> loaderResult = loader.call();
					if (loaderResult.getV() != null && loaderResult.isCache()) {
						t = loaderResult.getV();
						//缓存对应数据
						save(t, id, loaderResult.getExpireTime());
					}
				}
			}

		}
		return t;
	}

	/**
	 * 一次性查询多个id
	 * 存在bug不能用
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<T> multiFindByIds(final List<Long> ids) {
		List<Object> results = stringRedisTemplate().executePipelined(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {

				List<T> list = new ArrayList<T>();
				StringRedisConnection stringRedisConn = (StringRedisConnection) connection;

				for (long id : ids) {
					String key = MessageFormat.format(baseKey, id + "");
					System.out.println(key);
					Map<String, String> map = stringRedisConn.hGetAll(key);
					map.put("id", id + "");
					list.add(getBaseRedisMapper().fromHash(map));
				}

				return list;
			}
		});
		List<T> list = new ArrayList<T>();
		for (Object o : results) {
			T t = (T) o;
			list.add(t);
		}
		return list;
	}

	/**
	 * 保存对象
	 * @param t
	 * @param baseRedisMapper
	 * @return
	 */
	@Override
	public long save(T t) {
		//生成id
		String keyName = createKeyName();
		long id = makeId(keyName);
		Map<String, String> map = getBaseRedisMapper().toHash(t);
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().opsForHash().putAll(key, map);
		return id;
	}

	/**
	 * 
	 * @param t
	 * @param syncer
	 * @return 
	 */
	@Override
	public long save(T t, Syncer<T> syncer) {
		long id = save(t);
		//异步同步数据
		asyncData(id, syncer);
		return id;
	}

	/**
	 * 保存实体，
	 * @param t
	 * @param expireTime 过期时间
	 * @return
	 */
	@Override
	public long save(T t, final int expireTime) {
		long id = save(t);
		final String key = MessageFormat.format(baseKey, id + "");
		MethodHelper.expire(stringRedisTemplate(), key, expireTime);
		return id;
	}

	/**
	 * 
	 * @param t
	 * @param expireTime
	 * @param syncer
	 * @return 
	 */
	@Override
	public long save(T t, final int expireTime, Syncer<T> syncer) {
		long id = save(t, syncer);
		final String key = MessageFormat.format(baseKey, id + "");
		MethodHelper.expire(stringRedisTemplate(), key, expireTime);
		return id;
	}

	/**
	 * 保存一个有id的对象
	 * @param t
	 * @param id
	 * @param baseRedisMapper
	 * @return
	 */
	@Override
	public long save(T t, long id) {
		Map<String, String> map = getBaseRedisMapper().toHash(t);
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().opsForHash().putAll(key, map);
		return id;
	}

	/**
	 * 保存数据，id已存在
	 * @param t
	 * @param id
	 * @param syncer
	 * @return 
	 */
	@Override
	public long save(T t, long id, Syncer<T> syncer) {
		save(t, id);
		asyncData(id, syncer);
		return id;
	}

	/**
	 * 保存实体
	 * @param t
	 * @param id
	 * @param expireTime 过期时间
	 * @return
	 */
	@Override
	public long save(T t, long id, final int expireTime) {
		save(t, id);
		final String key = MessageFormat.format(baseKey, id + "");
		MethodHelper.expire(stringRedisTemplate(), key, expireTime);
		return id;
	}

	/**
	 * 保存有过期时间的数据
	 * @param t
	 * @param id
	 * @param expireTime 秒
	 * @param syncer
	 * @return 
	 */
	@Override
	public long save(T t, long id, final int expireTime, Syncer<T> syncer) {
		save(t, id, syncer);
		final String key = MessageFormat.format(baseKey, id + "");
		MethodHelper.expire(stringRedisTemplate(), key, expireTime);
		return id;
	}

	/**
	 * 判断id是否存在
	 * @param id
	 * @return
	 */
	@Override
	public boolean exists(long id) {
		String key = MessageFormat.format(baseKey, id + "");
		return stringRedisTemplate().hasKey(key);
	}

	/**
	 * 检查key值得过期时间
	 * 
	 * @param id
	 * @return 
	 */
	@Override
	public long ttl(long id) {
		final String key = MessageFormat.format(baseKey, id + "");
		return stringRedisTemplate().execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.ttl(key.getBytes());
			}
		});
	}

	/**
	 * 删除对应的id
	 * @param id
	 * @return
	 */
	@Override
	public long deleteById(long id) {
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().delete(key);
		return id;
	}

	/**
	 * 更新一个值
	 * 如果key存在的话，则更新redis中对应的key
	 * 不存在则忽略
	 * @param propertyName
	 * @param value
	 * @param id
	 * @return
	 */
	@Override
	public long updateByProperty(String propertyName, Object value, long id) {
		if (exists(id)) {
			String key = MessageFormat.format(baseKey, id + "");
			stringRedisTemplate().opsForHash().put(key, propertyName, String.valueOf(value));
		}
		return id;
	}

	/**
	 * 
	 * 更新多个属性
	 * 但key在redis中存在时才更新对应的值，
	 * 不存在则忽略
	 * @param map
	 * @param id
	 * @return
	 */
	@Override
	public long updateByMap(Map<String, String> map, long id) {
		if (exists(id)) {
			String key = MessageFormat.format(baseKey, id + "");
			stringRedisTemplate().opsForHash().putAll(key, map);
		}
		return id;
	}

	/**
	 * 增加某一个field的值
	 * @param haskField
	 * @param delta 负数表示减
	 * @param id
	 * @return
	 */
	@Override
	public long increment(String haskField, long delta, long id) {
		String key = MessageFormat.format(baseKey, id + "");
		return stringRedisTemplate().opsForHash().increment(key, haskField, delta);
	}

	/**
	 * 查询某一个属性对应的值
	 * @param propertyName
	 * @param id
	 * @return
	 */
	@Override
	public Object findByProperty(String propertyName, long id) {
		String key = MessageFormat.format(baseKey, id + "");
		return stringRedisTemplate().opsForHash().get(key, propertyName);
	}

	/**
	 * 
	 * @param propertyName
	 * @param id
	 * @param loader (重新reloader器)
	 * @return 
	 */
	@Override
	public Object findByProperty(String propertyName, long id, RedisLoader<T> loader) {
		Object o = null;
		if (isLocalCache) {
			//本地取数据
			T t = obtainByCache(id);
			if (t != null) {
				o = obtainValueByObjectProperty(t, propertyName);
			}
		}

		if (o == null) {
			if (!exists(id)) {
				T t = findByIdToRedis(id, loader);
				//反射拿到对应数据
				if (t != null) {
					o = obtainValueByObjectProperty(t, propertyName);
				}
			} else {
				String key = MessageFormat.format(baseKey, id + "");
				o = stringRedisTemplate().opsForHash().get(key, propertyName);
			}
		}
		return o;
	}

	/**
	 * 
	 * 
	 * @param t
	 * @param propertyName
	 * @return 
	 */
	private Object obtainValueByObjectProperty(T t, String propertyName) {
		Object o = null;
		try {
			o = ReflectUtil.invokeGetterMethod(t, propertyName,
					ReflectUtil.getOrderedFieldAndType(t).get(propertyName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 从缓存中获取数据
	 * 
	 * @param id
	 * @return 
	 */
	private T obtainByCache(long id) {
		T t = null;
		//需要本地缓存
		String key = MessageFormat.format(baseKey, id + "");
		try {
			t = initLocalCache().get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	private String createKeyName() {
		String entityClazzName = entityClazz.getSimpleName();
		String keyName = entityClazzName.substring(0, entityClazzName.lastIndexOf(Constant.Redis.ENTITY_SUFFIX));
		return keyName;
	}

	/**
	 * 初始化本地缓存
	 */
	private EntityLocalCache initLocalCache() {
		if (cache == null) {
			synchronized (lock) {
				if (cache == null) {
					cache = new EntityLocalCache(getCacheTime());
				}
			}
		}
		return cache;
	}

	/**
	 * 异步同步数据
	 * @param id
	 * @param syncer 
	 */
	private void asyncData(long id, Syncer<T> syncer) {
		T t = doGetById(id);
		if (t != null) {
			//异步同步数据
			executorService.execute(new SyncExecutor<T>(syncer, t));
		}
	}

	private long makeId(final String key) {

		return stringRedisTemplate().execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				long id = connection.incr(key.getBytes());

				if ((id + 75807) >= Long.MAX_VALUE) {
					// 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
					stringRedisTemplate().opsForValue().set(key, "0");
				}

				return id;
			}
		});
	}

	/**
	 * 持久化key值
	 * 检查对应的key是否存在，如果存在设置该key永不过期
	 * @param key
	 */
	@Override
	public boolean persistKey(int id) {
		boolean flag = false;
		if (exists(id)) {
			String key = MessageFormat.format(baseKey, id + "");
			flag = stringRedisTemplate().persist(key);
		}
		return flag;
	}

	public boolean isLocalCache() {
		return isLocalCache;
	}

	public void setLocalCache(boolean isLocalCache) {
		this.isLocalCache = isLocalCache;
	}

	public int getCacheTime() {
		return cacheTime == 0 ? DEFAULT_CAHCE_SECOND : cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	private void buildKey() {
		String keyName = createKeyName();
		this.baseKey = new StringBuffer().append(keyName).append(":{0}").toString();
		RegistryService.registry(baseKey);
	}

	/**
	 * 
	 * <b><code>EntityLocalCache</code></b>
	 * <p>
	 * 本地缓存
	 * </p>
	 */
	class EntityLocalCache extends AbstractLocalCache<T> {

		public EntityLocalCache(int second) {
			super(entityClazz.getSimpleName(), second);
		}

		@Override
		public T reloadData(List<String> ids) {
			return doGetById(Integer.parseInt(ids.get(ids.size() - 1)));
		}

	}

}

class SyncExecutor<T> implements Runnable {

	private Syncer<T> syncer;
	private T t;

	public SyncExecutor(Syncer<T> syncer, T t) {
		this.syncer = syncer;
		this.t = t;
	}

	@Override
	public void run() {
		syncer.excute(t);
	}

}
