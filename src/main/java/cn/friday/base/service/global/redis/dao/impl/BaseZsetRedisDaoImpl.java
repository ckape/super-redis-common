package cn.friday.base.service.global.redis.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.google.common.base.Joiner;

import cn.friday.base.service.global.redis.bo.SimpleTypeTuple;
import cn.friday.base.service.global.redis.bo.ZsetResult;
import cn.friday.base.service.global.redis.dao.IBaseZsetRedisDao;
import cn.friday.base.service.global.redis.dao.IRedisOpsTemplate;
import cn.friday.base.service.global.redis.registry.RegistryService;
import cn.friday.base.service.global.redis.util.MemberUtil;

/**
 * @author Zz
 */
public abstract class BaseZsetRedisDaoImpl<T> implements IBaseZsetRedisDao<T>, IRedisOpsTemplate {

	private String baseKey;

	private MemberUtil<T> memberUtil;

	public BaseZsetRedisDaoImpl(String baseKey, Class<T> entityClass) {
		this.baseKey = baseKey + ":{0}";
		RegistryService.registry(baseKey);
		memberUtil = new MemberUtil<T>(entityClass);
	}

	/**
	 * 增加
	 * @param member 
	 * @param score
	 * @param ids
	 * @return
	 */
	@Override
	public boolean add(T member, double score, int... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate().opsForZSet().add(key, memberUtil.getMember(member), score);
	}

	/**
	 * 增加多个成员
	 * @param tuples
	 * @param ids
	 * @return
	 */
	@Override
	public boolean add(Set<TypedTuple<String>> tuples, int... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate().opsForZSet().add(key, tuples) > 0 ? true : false;
	}

	/**
	 * 批量增加成员
	 * @param simpleTypeTuples
	 * @param ids
	 * @return
	 */
	@Override
	public boolean add(Collection<SimpleTypeTuple<T>> simpleTypeTuples, int... ids) {
		Set<TypedTuple<String>> tuples = new HashSet<TypedTuple<String>>();
		TypedTuple<String> member;
		for (SimpleTypeTuple<T> simple : simpleTypeTuples) {
			member = new DefaultTypedTuple<String>(memberUtil.getMember(simple.getValue()), simple.getScore());
			tuples.add(member);
		}
		return add(tuples, ids);
	}

	/**
	 * 增加指定元素的分值
	 * @param member
	 * @param delta
	 * @param ids
	 * @return 增加后的分值
	 */
	@Override
	public double incrScore(T member, double delta, int... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate().opsForZSet().incrementScore(key, memberUtil.getMember(member), delta);
	}

	/**
	 * 按照分值的升序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	@Override
	public Set<T> findByScoreAsc(double min, double max, int... ids) {
		String key = buildKey(ids);
		Set<String> members = stringRedisTemplate().opsForZSet().rangeByScore(key, min, max);
		return memberUtil.getSet(members);
	}

	/**
	 * 按照分值的降序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	@Override
	public Set<T> findByScoreDesc(double min, double max, int... ids) {
		String key = buildKey(ids);
		Set<String> members = stringRedisTemplate().opsForZSet().reverseRangeByScore(key, min, max);
		return memberUtil.getSet(members);
	}

	/**
	 * 按照分值的升序排序
	 * offset 从第几个开始
	 * count 总共多少条
	 */
	@Override
	public Set<T> findByScoreAsc(double min, double max, long offset, long count, int... ids) {
		String key = buildKey(ids);
		Set<String> members = stringRedisTemplate().opsForZSet().rangeByScore(key, min, max, offset, count);
		return memberUtil.getSet(members);
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
	@Override
	public Set<T> findByScoreDesc(double min, double max, long offset, long count, int... ids) {
		String key = buildKey(ids);
		Set<String> members = stringRedisTemplate().opsForZSet().reverseRangeByScore(key, min, max, offset, count);

		return memberUtil.getSet(members);
	}

	/**
	 * 查询一定区间的值
	 * 按照值的升序排序
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	@Override
	public Set<T> findByIdAsc(long start, long end, int... ids) {
		String key = buildKey(ids);
		Set<String> members = stringRedisTemplate().opsForZSet().range(key, start, end);
		return memberUtil.getSet(members);
	}

	/**
	 * 按照索引查询，
	 * 并返回分值
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	@Override
	public List<ZsetResult<T>> findByIdWithScoresAsc(long start, long end, int... ids) {
		List<ZsetResult<T>> zsetResults = new ArrayList<ZsetResult<T>>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate().boundZSetOps(key).rangeWithScores(start, end);
		for (TypedTuple<String> tt : results) {
			zsetResults.add(new ZsetResult<T>(memberUtil.getObject(tt.getValue()), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}

	/**
	 * 查询一定区间的值，
	 * 按照值的倒序排序
	 */
	@Override
	public Set<T> findByIdDesc(long start, long end, int... ids) {
		String key = buildKey(ids);
		Set<String> members = stringRedisTemplate().opsForZSet().reverseRange(key, start, end);
		return memberUtil.getSet(members);
	}

	/**
	 * 按照分值的顺序排序
	 */
	@Override
	public List<ZsetResult<T>> findByScoreWithScoresAsc(double min, double max, int... ids) {
		List<ZsetResult<T>> zsetResults = new ArrayList<ZsetResult<T>>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate().opsForZSet().rangeByScoreWithScores(key, min, max);
		for (TypedTuple<String> tt : results) {
			zsetResults.add(new ZsetResult<T>(memberUtil.getObject(tt.getValue()), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}

	/**
	 * 按照分值的倒序排序
	 */
	@Override
	public List<ZsetResult<T>> findByScoreWithScoresDesc(double min, double max, int... ids) {
		List<ZsetResult<T>> zsetResults = new ArrayList<ZsetResult<T>>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(key, min,
				max);
		for (TypedTuple<String> tt : results) {
			zsetResults.add(new ZsetResult<T>(memberUtil.getObject(tt.getValue()), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}

	/**
	 * 根据分值查询（升序）
	 * offset 从第几个开始查询
	 * count 查询多少个
	 */
	@Override
	public List<ZsetResult<T>> findByScoreWithScoresAsc(double min, double max, long offset, long count, int... ids) {
		List<ZsetResult<T>> zsetResults = new ArrayList<ZsetResult<T>>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate().opsForZSet().rangeByScoreWithScores(key, min, max,
				offset, count);
		for (TypedTuple<String> tt : results) {
			zsetResults.add(new ZsetResult<T>(memberUtil.getObject(tt.getValue()), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}

	/**
	 * 根据分值查询（降序）
	 * offset 从第几个开始查询
	 * count 查询多少个
	 */
	@Override
	public List<ZsetResult<T>> findByScoreWithScoresDesc(double min, double max, long offset, long count, int... ids) {
		List<ZsetResult<T>> zsetResults = new ArrayList<ZsetResult<T>>();
		String key = buildKey(ids);
		Set<TypedTuple<String>> results = stringRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(key, min,
				max, offset, count);
		for (TypedTuple<String> tt : results) {
			zsetResults.add(new ZsetResult<T>(memberUtil.getObject(tt.getValue()), tt.getScore()));
		}
		results = null;
		return zsetResults;
	}

	/**
	 * 查询成员对应的分值
	 * 无法确实member是否存在，还是score为0
	 * 推荐使用 score()
	 * @see score 
	 * @param member
	 * @param ids
	 * @return
	 */
	@Override
	@Deprecated
	public double getScore(T member, int... ids) {
		String key = buildKey(ids);
		Double score = stringRedisTemplate().opsForZSet().score(key, memberUtil.getMember(member));
		if (score == null) {
			return 0.0;
		}
		return score;
	}

	/**
	 * 查询成员对应的分值
	 * @param member
	 * @param ids
	 * @return -999999：表示元素在zset中不存在
	 */
	@Override
	public double score(T member, int... ids) {
		String key = buildKey(ids);
		Double score = stringRedisTemplate().opsForZSet().score(key, memberUtil.getMember(member));
		if (score == null) {
			return NO_MEMBER;
		}
		return score;
	}

	/**
	 * 判断成员是否存在
	 * @param member
	 * @param ids
	 * @return
	 */
	@Override
	public boolean existMember(T member, int... ids) {
		if (score(member, ids) == NO_MEMBER) {
			return false;
		}
		return true;
	}

	/**
	 * 长度
	 * @param ids
	 * @return
	 */
	@Override
	public long size(int... ids) {
		String key = buildKey(ids);
		Long size = stringRedisTemplate().opsForZSet().size(key);
		if (size == null) {
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
	@Override
	public long rangeSize(double min, double max, int... ids) {
		String key = buildKey(ids);
		Long size = stringRedisTemplate().opsForZSet().count(key, min, max);
		if (size == null) {
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
	@Override
	public boolean remove(T member, int... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate().opsForZSet().remove(key, memberUtil.getMember(member)) > 0 ? true : false;
	}

	/**
	 * 移除区间的成员
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	@Override
	public boolean removeRange(long start, long end, int... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate().opsForZSet().removeRange(key, start, end) > 0 ? true : false;
	}

	/**
	 * 移除分值区间的成员
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	@Override
	public boolean removeRangeByScore(double min, double max, int... ids) {
		String key = buildKey(ids);
		return stringRedisTemplate().opsForZSet().removeRangeByScore(key, min, max) > 0 ? true : false;
	}

	/**
	 * 删除key
	 * @param ids
	 * @return
	 */
	@Override
	public boolean deleteById(int... ids) {
		String key = buildKey(ids);
		stringRedisTemplate().delete(key);
		return true;
	}

	/**
	 * 设置过期时间
	 * @param seconds （秒）
	 * @param ids
	 */
	@Override
	public void expire(final long seconds, int... ids) {
		final String key = buildKey(ids);
		stringRedisTemplate().execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				Boolean flag = false;
				try {
					flag = connection.expire(key.getBytes(), seconds);
				} finally {
					connection.close();
				}
				return flag;
			}
		});
	}

	/**
	 * 获取第一个元素
	 * 按照从小到大
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	@Override
	public ZsetResult<T> getFirstWithScore(int... ids) {
		List<ZsetResult<T>> result = findByScoreWithScoresAsc(0, System.currentTimeMillis(), 0L, 1L, ids);
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 获取最后一个元素
	 * 按照从小到大
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	@Override
	public ZsetResult<T> getLastWithScore(int... ids) {
		List<ZsetResult<T>> result = findByScoreWithScoresDesc(0, System.currentTimeMillis(), 0L, 1L, ids);
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 获取列表中第一个元素
	 * 按照从小到大
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	@Override
	public T getFirst(int... ids) {
		Set<T> result = findByScoreAsc(0, System.currentTimeMillis(), 0L, 1L, ids);
		T retVal = null;
		if (result != null && !result.isEmpty()) {
			for (T t : result) {
				retVal = t;
				break;
			}
		}
		return retVal;
	}

	/**
	 * 获取列表中第一个元素
	 * 按照从小到大
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	@Override
	public T getLast(int... ids) {
		Set<T> result = findByScoreDesc(0, System.currentTimeMillis(), 0L, 1L, ids);
		T retVal = null;
		if (result != null && !result.isEmpty()) {
			for (T t : result) {
				retVal = t;
				break;
			}
		}
		return retVal;
	}

	private String buildKey(int... ids) {
		if (ids.length == 0) {
			return baseKey.replace(":{0}", "");
		} else {
			StringBuilder builder = new StringBuilder();
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}

			Joiner.on(":").appendTo(builder, list);
			return MessageFormat.format(baseKey, builder.toString());
		}

	}

}
