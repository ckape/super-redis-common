package cn.friday.base.service.global.redis.dao;

import java.util.List;
import java.util.Map;

import cn.friday.base.service.global.redis.mapper.IBaseRedisMapper;


public interface IBaseHashRedisDao<T> {
	
	public T findById(long id);
	
	/**
	 * 一次性查询多个id
	 * @param ids
	 * @return
	 */
	public List<T> multiFindByIds(final List<Long> ids);
	
	public long save(T t);
	
	/**
	 * 保存实体，
	 * @param t
	 * @param expireTime 过期时间
	 * @return
	 */
	public long save(T t, int expireTime);
	
	public long save(T t, long id);
	
	/**
	 * 保存实体
	 * @param t
	 * @param id
	 * @param expireTime 过期时间
	 * @return
	 */
	public long save(T t, long id,int expireTime);
	
	public boolean exists(long id);
	
	public long deleteById(long id);
	
	public  long updateByProperty(String propertyName, Object value, long id) ;
	
	public long updateByMap( Map<String, String> map, long id);
	
	public IBaseRedisMapper<T> getBaseRedisMapper();

}
