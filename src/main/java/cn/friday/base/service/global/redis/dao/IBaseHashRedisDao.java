package cn.friday.base.service.global.redis.dao;

import java.util.List;
import java.util.Map;


public interface IBaseHashRedisDao<T> {
	
	public T findById(long id);
	
	/**
	 * 一次性查询多个id
	 * @param ids
	 * @return
	 */
	public List<T> multiFindByIds(final List<Long> ids);
	
	public long save(T t);
	
	public long save(T t, long id);
	
	public boolean exists(long id);
	
	public long deleteById(long id);
	
	public  long updateByProperty(String propertyName, Object value, long id) ;
	
	public long updateByMap( Map<String, String> map, long id);

}
