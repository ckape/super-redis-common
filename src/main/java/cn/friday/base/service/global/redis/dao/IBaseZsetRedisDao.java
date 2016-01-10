package cn.friday.base.service.global.redis.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import cn.friday.base.service.global.redis.bo.SimpleTypeTuple;
import cn.friday.base.service.global.redis.bo.ZsetResult;

public interface IBaseZsetRedisDao<T> {
	
	
	/**
	 * 增加
	 * @param member 
	 * @param score
	 * @param ids
	 * @return
	 */
	public boolean add( T member,  double score,int ... ids);
	
	/**
	 * 增加多个成员
	 * @param tuples
	 * @param ids
	 * @return
	 */
	public boolean add(Set<TypedTuple<String>> tuples, int ... ids);
	
	/**
	 * 批量增加成员
	 * @param simpleTypeTuples
	 * @param ids
	 * @return
	 */
	public boolean add(Collection<SimpleTypeTuple<T>> simpleTypeTuples, int... ids );
	
	/**
	 * 增加指定元素的分值
	 * @param member
	 * @param delta
	 * @param ids
	 * @return 增加后的分值
	 */
	public double incrScore(T member, double delta, int ... ids);
	
	/**
	 *  按照分值的降序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public Set<T> findByScoreDesc(double min,double max,int ... ids);
	
	/**
	 * 按照分值的排序（升序）
	 * @param min
	 * @param max
	 * @param offset 从第几条开始
	 * @param count
	 * @param ids
	 * @return
	 */
	public Set<T> findByScoreAsc(double min,double max, long offset, long count, int ... ids);
	
	/**
	 * 按照分值的排序（降序）
	 * @param min
	 * @param max
	 * @param offset 从第几条开始
	 * @param count
	 * @param ids
	 * @return
	 */
	public Set<T> findByScoreDesc(double min,double max,  long offset, long count, int ... ids);
	
	/**
	 * 按照分值的升序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public Set<T> findByScoreAsc(double min,double max,int ... ids);
	
	/**
	 * 查询一定区间的值
	 * 按照值的升序排序
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public Set<T> findByIdAsc(long start,long end, int ... ids);
	
	/**
	 * 按照索引查询，
	 * 并返回分值
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 *@author BravoZu
	 */
	public List<ZsetResult<T>> findByIdWithScoresAsc(long start,long end, int ... ids);
	
	/**
	 * 查询一定区间的值，
	 * 按照值的倒序排序
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public Set<T> findByIdDesc(long start,long end, int ... ids);
	/**
	 * 按照分值的顺序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public List<ZsetResult<T>> findByScoreWithScoresAsc(double min,double max, int ... ids);
	/**
	 * 按照分值的倒序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public List<ZsetResult<T>> findByScoreWithScoresDesc(double min,double max, int ... ids);
	
	/**
	 * 根据分值查询（升序）
	 * @param min
	 * @param max
	 * @param offset 第几个开始
	 * @param count 查询总的数量
	 * @param ids
	 * @return
	 */
	public List<ZsetResult<T>> findByScoreWithScoresAsc(double min,double max, long offset, long count,int ... ids);
	
	/**
	 * 根据分值查询（降序）
	 * @param min
	 * @param max
	 * @param offset 第几个开始
	 * @param count 查询总的数量
	 * @param ids
	 * @return
	 */
	public List<ZsetResult<T>> findByScoreWithScoresDesc(double min,double max, long offset, long count,int ... ids);
	
	/**
	 * 查询成员对应的分值
	 * @param member
	 * @param ids
	 * @return
	 */
	public double getScore(T member,int ... ids);
	
	/**
	 * 判断成员是否存在
	 * @param member
	 * @param ids
	 * @return
	 */
	public boolean existMember(T member,int ... ids);
	
	/**
	 * 长度
	 * @param ids
	 * @return
	 */
	public long size(int ... ids);
	
	/**
	 * 区间长度
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public long rangeSize(double min,double max,int ... ids);
	
	/**
	 * 删除成员
	 * @param member
	 * @param ids
	 * @return
	 */
	public boolean remove(T member,int ... ids);
	
	/**
	 * 移除区间的成员
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public boolean removeRange(long start, long end, int... ids);
	
	/**
	 * 移除分值区间的成员
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public boolean removeRangeByScore(double min,double max,int... ids);
	
	/**
	 * 删除key
	 * @param ids
	 * @return
	 */
	public boolean deleteById(int... ids );
	
	/**
	 * 设置过期时间
	 * @param seconds （秒）
	 * @param ids
	 */
	public void expire(final long seconds, int... ids);
	

}
