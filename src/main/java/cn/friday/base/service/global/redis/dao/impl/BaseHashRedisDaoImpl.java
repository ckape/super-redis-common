package cn.friday.base.service.global.redis.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import cn.friday.base.service.global.redis.dao.IBaseHashRedisDao;
import cn.friday.base.service.global.redis.dao.IRedisOpsTemplate;

public abstract class BaseHashRedisDaoImpl<T> implements IBaseHashRedisDao<T>, IRedisOpsTemplate {
	
	private Class<T> entityClazz;
	
	private String baseKey;
	
	public BaseHashRedisDaoImpl(Class<T> entityClazz) {
		this.entityClazz = entityClazz;
		buildKey();
	}

	@Override
	public T findById(long id) {
		String key = MessageFormat.format(baseKey, id + "");
		Map<Object, Object> entityMap = stringRedisTemplate().opsForHash().entries(key);
		entityMap.put("id", id);
		return getBaseRedisMapper().fromObjectHash(entityMap);
	}
	
	/**
	 * 一次性查询多个id
	 * 存在bug不能用
	 * @param ids
	 * @return
	 */
	@Deprecated
	public List<T> multiFindByIds(final List<Long> ids){
		List<Object> results = stringRedisTemplate().executePipelined(new RedisCallback<List<T>>() {
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				
				List<T> list = new ArrayList<T>();
				StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
				
				for(long id:ids){
					String key = MessageFormat.format(baseKey, id + "");
					System.out.println(key);
					Map<String,String> map = stringRedisConn.hGetAll(key);
					System.out.println(map);
					map.put("id", id+"");
					list.add(getBaseRedisMapper().fromHash(map));
				}
				
				return list;
			}
		});
		List<T> list = new ArrayList<T>();
		for(Object o:results){
			@SuppressWarnings("unchecked")
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
	public long save(T t){
		//生成id
		String keyName =  createKeyName();
		long id = makeId(keyName);
		Map<String, String> map = getBaseRedisMapper().toHash(t);
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().opsForHash().putAll(key, map);
		return id;
	}
	/**
	 * 保存实体，
	 * @param t
	 * @param expireTime 过期时间
	 * @return
	 */
	public long save(T t, int expireTime){
		long id = save(t);
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().expire(key, expireTime, TimeUnit.SECONDS);
		return id;
	}
	
	/**
	 * 保存一个有id的对象
	 * @param t
	 * @param id
	 * @param baseRedisMapper
	 * @return
	 */
	public long save(T t, long id){
		Map<String, String> map = getBaseRedisMapper().toHash(t);
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().opsForHash().putAll(key, map);
		return id;
	}
	
	/**
	 * 保存实体
	 * @param t
	 * @param id
	 * @param expireTime 过期时间
	 * @return
	 */
	public long save(T t, long id,int expireTime){
	      save(t, id);
	      String key = MessageFormat.format(baseKey, id + "");  
	      stringRedisTemplate().expire(key, expireTime, TimeUnit.SECONDS);
	      return id;
	}
	
	/**
	 * 判断id是否存在
	 * @param id
	 * @return
	 */
	public boolean exists(long id){
		String key = MessageFormat.format(baseKey, id + "");
		return stringRedisTemplate().getConnectionFactory().getConnection().exists(key.getBytes());
	}
	
	/**
	 * 删除对应的id
	 * @param id
	 * @return
	 */
	public long deleteById(long id) {
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().delete(key);
		return id;
	}
	
	/**
	 * 更新一个值
	 * @param propertyName
	 * @param value
	 * @param id
	 * @return
	 */
	public  long updateByProperty(String propertyName, Object value, long id) {
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().opsForHash().put(key, propertyName, String.valueOf(value));
		return id;
	}
	
	/**
	 * 更新多个属性
	 * @param map
	 * @param id
	 * @return
	 */
	public long updateByMap( Map<String, String> map, long id) {
		String key = MessageFormat.format(baseKey, id + "");
		stringRedisTemplate().opsForHash().putAll(key, map);
		return id;
	} 
	
	/**
	 * 增加某一个field的值
	 * @param haskField
	 * @param delta 负数表示减
	 * @param id
	 * @return
	 */
	public boolean increment(String haskField,long delta, long id){
		String key = MessageFormat.format(baseKey, id + "");
		return stringRedisTemplate().opsForHash().increment(key, haskField, delta) > 0 ? true : false;
	}
	
	private String createKeyName(){
		String entityClazzName = entityClazz.getSimpleName();
		String keyName = entityClazzName.substring(0,entityClazzName.lastIndexOf("Redis"));
		return keyName;
	}
	
	private long makeId(String key){
		long id = stringRedisTemplate().getConnectionFactory().getConnection().incr(key.getBytes());
		if( (id + 75807) >= Long.MAX_VALUE ){
			// 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
			stringRedisTemplate().opsForValue().set(key, "0");
		}
		return id;
	}
	
	private void buildKey() {
		String keyName = createKeyName();
		this.baseKey = new StringBuffer().append(keyName).append(":{0}").toString();
	}
	
}
