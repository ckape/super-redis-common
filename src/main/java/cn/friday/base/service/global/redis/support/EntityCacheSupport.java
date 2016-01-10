package cn.friday.base.service.global.redis.support;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import cn.friday.base.service.global.redis.dao.IBaseZsetRedisDao;
import cn.friday.common.service.global.common.Constant;
import static com.google.common.base.Preconditions.*;


/**
 * 实体缓存的帮助方法
 * 支持批量缓存数据或
 * 获取数据
 * @author BravoZu
 *
 * @param <T>
 */
public abstract class EntityCacheSupport<T> extends DynamicFieldHashRedisDao{
	
	//默认过期时间
	
	private final static long DEFAULT_SECONDS = 60;
	
	private final long seconds ;

	private final static String REDIS_CACHE_SUFFIX =".Cache";

	private Class<T> entityClazz;
	
	private String baseKey;
	
	public EntityCacheSupport(Class<T> entityClazz){
		this(entityClazz, DEFAULT_SECONDS);
	}
	
	public EntityCacheSupport(Class<T> entityClazz, long seconds){
		this.entityClazz = entityClazz;
		this.seconds = seconds;
		createKey();
	}
	
	/**
	 * 保存缓存数据
	 * @param entityId
	 * @param t
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	public boolean save(long entityId, T t, int... ids){
		
		checkNotNull(t, "数据不能为空");
		
		String key = buildKey(ids);
		Map<String, String> map =  transData(entityId, t);
		return save(key, map, seconds);
	}
	
	/**
	 * 批量缓存数据
	 * @param map
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	public boolean save(Map<Long, T> map, int... ids){
		 checkArgument( !(map == null || map.isEmpty()), "map 数据不能为空");
		 String key = buildKey(ids);
		 Map<String, String> tagertMap = transData(map);
		return save(key, tagertMap, seconds);
	}
	
	/**
	 * 获取缓存中的数据
	 * @param members
	 * @param id
	 * @return
	 *@author BravoZu
	 */
	private List<T> getCache(Set<Long> members, int... ids){
		 List<T> list = new ArrayList<T>();
		 String key = buildKey(ids);
		 Set<String> fields =changeString(members);
		 List<String> values = this.getByFields(key, fields);
		 if(values != null && values.size() > 0){
			 for( String str:values){
				 if( !Strings.isNullOrEmpty(str)){
					 list.add( JSON.parseObject(str, entityClazz) );
				 }
			 }
		 }
		return list;
	}
	
	/**
	 * 分析缓存中的数据
	 * @param members
	 * @param list
	 * @return
	 *@author BravoZu
	 */
	 protected abstract EntityCacheResult<T> analyzeCache(Set<Long> members, List<T> list);
	
	/**
	 * 从缓存中获取数据，
	 * 如果数据存在的话
	 * @param members
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	public EntityCacheResult<T> getCacheIfExist(Set<Long> members, int... ids){
		List<T> list = getCache(members, ids);
		return analyzeCache(members, list);
	}
	

	/**
	 * 检查是否需要缓存(某一时间区间内的)
	 * @param baseZsetRedisDao
	 * @param cacheNum
	 * @param timestamp
	 * @return
	 *@author BravoZu
	 */
	@SuppressWarnings("rawtypes")
	public boolean checkNeedCache(IBaseZsetRedisDao baseZsetRedisDao, int cacheNum, long timestamp, int... ids){
		if(baseZsetRedisDao.rangeSize(timestamp, Constant.Redis.MAX_TIME_LONG, ids) < cacheNum){
			return true;
		}
		return false;
	}
	
	/**
	 * 删除缓存对应的Field
	 * @param entityId
	 * @param ids
	 *@author BravoZu
	 */
	public void delCacheByEntityId(long entityId, int... ids){
		String key = buildKey(ids);
		delByField(key, String.valueOf(entityId));
	}
	
	
	
	
	/**
	 * 转换数据
	 * @param entityId
	 * @param t
	 * @return
	 *@author BravoZu
	 */
	private Map<String, String> transData(long entityId, T t){
		Map<String, String> map = new HashMap<String, String>();
		map.put(String.valueOf(entityId), JSON.toJSONString(t));
		return map;
	}
	
	/**
	 * 转换生成对应数据
	 * @param sourceMap
	 * @return
	 *@author BravoZu
	 */
	private Map<String, String> transData(Map<Long, T> sourceMap){
		Map<String, String> map = new HashMap<String, String>();
		for(long id:sourceMap.keySet()){
			map.put(String.valueOf(id), JSON.toJSONString(sourceMap.get(id)));
		}
		return map;
	}

	
	/**
	 * 转换对应数据的类型
	 * @param members
	 * @return
	 *@author BravoZu
	 */
	private Set<String> changeString(Set<Long> members){
		 Set<String> result = new HashSet<String>(members.size());
		 for(long id:members){
			 result.add(String.valueOf(id));
		 }
		 return result;
		 
	}
	
	private String createKeyName(){
		String entityClazzName = entityClazz.getSimpleName();
		return entityClazzName;
	}
	
	private void createKey() {
		String keyName = createKeyName();
		this.baseKey = new StringBuffer().append(keyName).append(REDIS_CACHE_SUFFIX).append(":{0}").toString();
	}
	
	/**
	 * 构造key
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	private String buildKey(int... ids){
		
		if(ids.length == 0){
			return baseKey.replace(":{0}", "");
		}else{
			StringBuilder builder = new StringBuilder();
			List<Integer> list = new ArrayList<Integer>();
			for(int i = 0;i < ids.length;i++ ){
				list.add(ids[i]);
			}
		     Joiner.on(":").appendTo(builder, list);
		     return MessageFormat.format(baseKey, builder.toString());
		}
	}
	
}
