package cn.friday.base.service.global.redis.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;

import cn.friday.base.service.global.redis.util.BooleanRedisConvert;
import cn.friday.base.service.global.redis.util.DateRedisConvert;

public class BaseRedisMapper<T> extends BeanUtilsHashMapper<T> implements IBaseRedisMapper<T>{
	
	
	static{
		//注册转换帮助类
		ConvertUtils.register(new DateRedisConvert(), Date.class);
		ConvertUtils.register(new BooleanRedisConvert() , Boolean.class);
	}

	

	public BaseRedisMapper(Class<T> type) {
		super(type);
	}
	
	public T fromObjectHash(Map<Object, Object> hash) {
		
		Map<String,String> map = new HashMap<String, String>();
		
		for(Object key:hash.keySet()){
			String keyStr = key.toString();
			String valueStr = hash.get(key).toString();
			if(keyStr.startsWith("is") && ( ("true").equals(valueStr) || ("false").equals(valueStr)) ){
				keyStr = keyStr.substring(2);
				keyStr = keyStr.substring(0, 1).toLowerCase()+keyStr.substring(1, keyStr.length());
			
			}
			
			map.put(keyStr, valueStr);
		}
		
		return super.fromHash(map);
	}
	
	public T fromHash(Map<String, String> hash) {
		Map<String,String> map = new HashMap<String, String>();
		for(String key:hash.keySet()){
			String value = hash.get(key);
			if(key.startsWith("is") && ( ("true").equals(value) || ("false").equals(value))){
				key = key.substring(2);
				key = key.substring(0, 1).toLowerCase()+key.substring(1, key.length());
			}
			map.put(key, value);
		}
		return super.fromHash(map);
	}
	
	public Map<String, String> toHash(T object){
		return super.toHash(object);
	}
	
	
}
