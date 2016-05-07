package cn.friday.base.service.global.redis.dao;

import java.util.List;
import java.util.Map;

import cn.friday.base.service.global.redis.loader.RedisLoader;
import cn.friday.base.service.global.redis.mapper.IBaseRedisMapper;
import cn.friday.base.service.global.redis.syncer.Syncer;

public interface IBaseHashRedisDao<T> {

	public T findById(long id);

	/**
	 * 
	 * @param id
	 * @param loader (当redis中不存在时，如何恢复，继承这个抽象类)
	 * @return 下午7:46:13
	 * 2016年5月6日
	 */
	public T findById(long id, RedisLoader<T> loader);

	/**
	 * 一次性查询多个id
	 * @param ids
	 * @return
	 */
	public List<T> multiFindByIds(final List<Long> ids);

	public long save(T t);

	/**
	 * 
	 * @param t
	 * @param syncer
	 * @return 
	 */
	public long save(T t, Syncer<T> syncer);

	/**
	 * 保存实体，
	 * @param t
	 * @param expireTime 过期时间
	 * @return
	 */
	public long save(T t, int expireTime);

	/**
	 * 
	 * @param t
	 * @param expireTime
	 * @param syncer
	 * @return 
	 */
	public long save(T t, final int expireTime, Syncer<T> syncer);

	public long save(T t, long id);

	/**
	 * 保存数据，id已存在
	 * @param t
	 * @param id
	 * @param syncer
	 * @return 
	 */
	public long save(T t, long id, Syncer<T> syncer);

	/**
	 * 保存实体
	 * @param t
	 * @param id
	 * @param expireTime 过期时间
	 * @return
	 */
	public long save(T t, long id, int expireTime);

	/**
	 * 保存有过期时间的数据
	 * @param t
	 * @param id
	 * @param expireTime 秒
	 * @param syncer
	 * @return 
	 */
	public long save(T t, long id, final int expireTime, Syncer<T> syncer);

	public boolean exists(long id);

	public long deleteById(long id);

	public long updateByProperty(String propertyName, Object value, long id);

	public long updateByMap(Map<String, String> map, long id);

	/**
	 * 增加某一个field的值
	 * @param haskField
	 * @param delta 负数表示减
	 * @param id
	 * @return
	 */
	public long increment(String haskField, long delta, long id);

	/**
	 * 查询某一个属性对应的值
	 * @param propertyName
	 * @param id
	 * @return
	 */
	public Object findByProperty(String propertyName, long id);

	public IBaseRedisMapper<T> getBaseRedisMapper();

	/**
	 * 持久化key值
	 * 如果有过期时间的话
	 * 设置成永不过期
	 * @param key
	 */
	public boolean persistKey(int id);

}
