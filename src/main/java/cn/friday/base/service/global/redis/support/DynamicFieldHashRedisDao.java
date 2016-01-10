package cn.friday.base.service.global.redis.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.BoundHashOperations;

import static com.google.common.base.Preconditions.*;
import cn.friday.base.service.global.redis.dao.IRedisOpsTemplate;
import cn.friday.base.service.global.redis.util.MethodHelper;

/**
 * 动态field的hash操作
 * @author BravoZu
 *
 */
public abstract class DynamicFieldHashRedisDao implements IRedisOpsTemplate{

	//设置默认的过期时间
	private final long defaultSeconds = 60;
	
	/**
	 * 保存
	 * @param key
	 * @param map
	 * @param seconds
	 * @return
	 *@author BravoZu
	 */
	public boolean save(String key, Map<String, String> map, long seconds){
		
		 checkNotNull(key, "key 不能为空");
		 checkArgument(  !(map == null || map.isEmpty()) , "map 数据不能为空");
		
		 boolean hasExpire = false;
		 BoundHashOperations<String, Object, Object> oper = stringRedisTemplate().boundHashOps(key);
		 if( !stringRedisTemplate().hasKey(key)){
			 hasExpire = true;
		 }
		 oper.putAll(map);
		 
		 //设置过期时间
		if(hasExpire){
			if( seconds == 0){
				//设置默认过期时间
				seconds = defaultSeconds;
			}
			MethodHelper.expire(stringRedisTemplate(), key, seconds);
		}
		
		return true;
		
	}
	
	/**
	 * 更新数据操作
	 * @param key
	 * @param map
	 * @return
	 *@author BravoZu
	 */
	public boolean update(String key, Map<String, String> map){
		return save(key, map, defaultSeconds);
	}
	
	/**
	 * 更新属性
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 *@author BravoZu
	 */
	public boolean updateByProperty(String key, String field, String value ){
		Map<String, String> map = new HashMap<String, String>();
		map.put(field, value);
		return save(key, map, defaultSeconds);
	}
	
	/**
	 * 获取fields集合对应的值
	 * @param key
	 * @param fields
	 * @return
	 *@author BravoZu
	 */
	public List<String> getByFields(String key,Collection<String> fields){
		
		 checkNotNull(key, "key 不能为空");
		 checkArgument( !(fields == null || fields.size() == 0) , "fields 不能为空");
		 
		 BoundHashOperations<String, String, String> oper = stringRedisTemplate().boundHashOps(key);
		 return oper.multiGet(fields);
	}
	
	/**
	 * 获取单一field的值
	 * @param key
	 * @param field
	 * @return
	 *@author BravoZu
	 */
	public String getByField(String key, String field){
		
		checkNotNull(key, "key 不能为空");
		checkNotNull(field, "field 不能为空");
		BoundHashOperations<String, String, String> oper = stringRedisTemplate().boundHashOps(key);
		return oper.get(field);
	}
	
	/**
	 * 删除对应的field
	 * @param key
	 * @param field
	 *@author BravoZu
	 */
	public void delByField(String key, String field){
		checkNotNull(key, "key 不能为空");
		checkNotNull(field, "field 不能为空");
		BoundHashOperations<String, String, String> oper = stringRedisTemplate().boundHashOps(key);
		oper.delete(field);
	}

}
