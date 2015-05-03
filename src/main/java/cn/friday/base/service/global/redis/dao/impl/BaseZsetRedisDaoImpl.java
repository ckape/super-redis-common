package cn.friday.base.service.global.redis.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import com.google.common.base.Joiner;
import cn.friday.base.service.global.redis.bo.ZsetResult;
import cn.friday.base.service.global.redis.dao.IBaseZsetRedisDao;

/**
 * @author Zz
 */
public class BaseZsetRedisDaoImpl implements IBaseZsetRedisDao {
	
	private String baseKey;
	
	@Resource
	StringRedisTemplate stringRedisTemplate; 
	
	public BaseZsetRedisDaoImpl(String baseKey){
		this.baseKey = baseKey+":{0}";
	}
	
	/**
	 * 增加
	 * @param member 
	 * @param score
	 * @param ids
	 * @return
	 */
	public boolean add( String member,  double score,int ... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().add(key, member, score);
	}
	
	/**
	 * 增加指定元素的分值
	 * @param member
	 * @param delta
	 * @param ids
	 * @return 增加后的分值
	 */
	public double incrScore(String member, double delta, int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().incrementScore(key, member, delta);
	} 
	
	/**
	 * 按照分值的升序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreAsc(double min,double max,int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
	}
	
	/**
	 * 按照分值的降序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreDesc(double min,double max,int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}
	
	/**
	 * 按照分值的升序排序
	 * offset 从第几个开始
	 * count 总共多少条
	 */
	public Set<String> findByScoreAsc(double min,double max, long offset, long count, int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
	}
	
	/**
	 * 按照分值的排序（降序）
	 * @param min
	 * @param max
	 * @param offset 从第几条开始
	 * @param count
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreDesc(double min,double max,  long offset, long count, int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
	}
	

	/**
	 * 查询一定区间的值
	 * 按照值的升序排序
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public Set<String> findByIdAsc(long start,long end, int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().range(key, start, end);
	}
	
	/**
	 * 查询一定区间的值，
	 * 按照值的倒序排序
	 */
	public Set<String> findByIdDesc(long start,long end, int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
	}
	
	/**
	 * 按照分值的顺序排序
	 */
	public List<ZsetResult> findByScoreWithScoresAsc(double min,double max, int ... ids){
		List<ZsetResult> zsetResults = new ArrayList<ZsetResult>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
		for(TypedTuple<String>  tt: results){
			zsetResults.add(new ZsetResult(tt.getValue(), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}
	
	/**
	 * 按照分值的倒序排序
	 */
	public List<ZsetResult> findByScoreWithScoresDesc(double min,double max, int ... ids){
		List<ZsetResult> zsetResults = new ArrayList<ZsetResult>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
		for(TypedTuple<String>  tt: results){
			zsetResults.add(new ZsetResult(tt.getValue(), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}
	
	/**
	 * 根据分值查询（升序）
	 * offset 从第几个开始查询
	 * count 查询多少个
	 */
	public List<ZsetResult> findByScoreWithScoresAsc(double min,double max, long offset, long count,int ... ids){
		List<ZsetResult> zsetResults = new ArrayList<ZsetResult>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
		for(TypedTuple<String>  tt: results){
			zsetResults.add(new ZsetResult(tt.getValue(), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}
	
	/**
	 * 根据分值查询（降序）
	 * offset 从第几个开始查询
	 * count 查询多少个
	 */
	public List<ZsetResult> findByScoreWithScoresDesc(double min,double max, long offset, long count,int ... ids){
		List<ZsetResult> zsetResults = new ArrayList<ZsetResult>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
		for(TypedTuple<String>  tt: results){
			zsetResults.add(new ZsetResult(tt.getValue(), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}
	
	/**
	 * 查询成员对应的分值
	 * @param member
	 * @param ids
	 * @return
	 */
	public double getScore(String member,int ... ids){
		String key = buildKey(ids);
		Double score = stringRedisTemplate.opsForZSet().score(key, member);
		if(score == null){
			return 0.0;
		}
		return score;
	}
	
	/**
	 * 判断成员是否存在
	 * @param member
	 * @param ids
	 * @return
	 */
	public boolean existMember(String member,int ... ids){
		if(getScore(member, ids) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 长度
	 * @param ids
	 * @return
	 */
	public long size(int ... ids){
		 String key = buildKey(ids);
		 Long size = stringRedisTemplate.opsForZSet().size(key);
		 if(size == null){
			 return 0;
		 }
		 return size;
	}
	/**
	 * 区间长度
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public long rangeSize(double min,double max,int ... ids){
		String key = buildKey(ids);
		Long size = stringRedisTemplate.opsForZSet().count(key, min, max);
		if(size == null){
			return 0;
		}
		return size;
	}
	/**
	 * 删除成员
	 * @param member
	 * @param ids
	 * @return
	 */
	public boolean remove(String member,int ... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().remove(key, member) > 0 ?  true : false;
	}
	
	/**
	 * 移除区间的成员
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public boolean removeRange(long start, long end, int... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().removeRange(key, start, end) > 0 ? true : false;
	}
	
	/**
	 * 移除分值区间的成员
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public boolean removeRangeByScore(double min,double max,int... ids){
		String key = buildKey(ids);
		return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max) > 0 ? true : false;
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
