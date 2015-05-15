package cn.friday.base.service.global.redis.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.common.base.Joiner;

import cn.friday.base.service.global.redis.dao.IBaseListRedisDao;

public class BaseListRedisDaoImpl implements IBaseListRedisDao {
	
	private String baseKey;
	
	@Resource
	StringRedisTemplate stringRedisTemplate; 
	
	public BaseListRedisDaoImpl(String baseKey){
		this.baseKey = baseKey+":{0}";
	}
	
	/**
	 * 从左边入队列一个元素
	 * @param member
	 * @param ids
	 * @return
	 */
	public long lpush(String member,int... ids){
		return boundListOps(ids).leftPush(member);
	}
	
	/**
	 * 从右边出队列
	 * @param ids
	 * @return
	 */
	public String rpop(int... ids){
		return boundListOps(ids).rightPop();
	}
	/**
	 * 获取长度
	 * @param ids
	 * @return
	 */
	public long size(int... ids){
		return boundListOps(ids).size();
	}
	
	private BoundListOperations<String, String> boundListOps(int... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.boundListOps(key);
	}
	
	private String buildKey(int ... ids){
		StringBuilder builder = new StringBuilder();
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i < ids.length;i++ ){
			list.add(ids[i]);
		}
		
	     Joiner.on(":").appendTo(builder, list);
	     return MessageFormat.format(baseKey, builder.toString());
	}

}
