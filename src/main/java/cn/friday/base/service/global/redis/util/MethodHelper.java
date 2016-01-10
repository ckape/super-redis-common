package cn.friday.base.service.global.redis.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;


public class MethodHelper {
	
	/**
	 * 设置过期时间
	 * @param stringRedisTemplate
	 * @param key
	 * @param seconds
	 * @return
	 *@author BravoZu
	 */
	public static boolean expire(StringRedisTemplate stringRedisTemplate, final String key, final long seconds){
		stringRedisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				Boolean flag =false;
				try{
					flag =  connection.expire(key.getBytes(), seconds);
				}finally{
					connection.close();
				}
				return flag;
			}
		});
		return true;
	}
	
}
