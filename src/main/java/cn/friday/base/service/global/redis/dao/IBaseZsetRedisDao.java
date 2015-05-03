package cn.friday.base.service.global.redis.dao;

import java.util.List;
import java.util.Set;

import cn.friday.base.service.global.redis.bo.ZsetResult;

public interface IBaseZsetRedisDao {
	
	
	/**
	 * 增加
	 * @param member 
	 * @param score
	 * @param ids
	 * @return
	 */
	public boolean add( String member,  double score,int ... ids);
	
	/**
	 * 增加指定元素的分值
	 * @param member
	 * @param delta
	 * @param ids
	 * @return 增加后的分值
	 */
	public double incrScore(String member, double delta, int ... ids);
	
	/**
	 *  按照分值的降序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreDesc(double min,double max,int ... ids);
	
	/**
	 * 按照分值的排序（升序）
	 * @param min
	 * @param max
	 * @param offset 从第几条开始
	 * @param count
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreAsc(double min,double max, long offset, long count, int ... ids);
	
	/**
	 * 按照分值的排序（降序）
	 * @param min
	 * @param max
	 * @param offset 从第几条开始
	 * @param count
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreDesc(double min,double max,  long offset, long count, int ... ids);
	
	/**
	 * 按照分值的升序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public Set<String> findByScoreAsc(double min,double max,int ... ids);
	
	/**
	 * 查询一定区间的值
	 * 按照值的升序排序
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public Set<String> findByIdAsc(long start,long end, int ... ids);
	
	/**
	 * 查询一定区间的值，
	 * 按照值的倒序排序
	 * @param start
	 * @param end
	 * @param ids
	 * @return
	 */
	public Set<String> findByIdDesc(long start,long end, int ... ids);
	/**
	 * 按照分值的顺序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public List<ZsetResult> findByScoreWithScoresAsc(double min,double max, int ... ids);
	/**
	 * 按照分值的倒序排序
	 * @param min
	 * @param max
	 * @param ids
	 * @return
	 */
	public List<ZsetResult> findByScoreWithScoresDesc(double min,double max, int ... ids);
	
	/**
	 * 根据分值查询（升序）
	 * @param min
	 * @param max
	 * @param offset 第几个开始
	 * @param count 查询总的数量
	 * @param ids
	 * @return
	 */
	public List<ZsetResult> findByScoreWithScoresAsc(double min,double max, long offset, long count,int ... ids);
	
	/**
	 * 根据分值查询（降序）
	 * @param min
	 * @param max
	 * @param offset 第几个开始
	 * @param count 查询总的数量
	 * @param ids
	 * @return
	 */
	public List<ZsetResult> findByScoreWithScoresDesc(double min,double max, long offset, long count,int ... ids);
	
	/**
	 * 查询成员对应的分值
	 * @param member
	 * @param ids
	 * @return
	 */
	public double getScore(String member,int ... ids);
	
	/**
	 * 判断成员是否存在
	 * @param member
	 * @param ids
	 * @return
	 */
	public boolean existMember(String member,int ... ids);
	
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
	public boolean remove(String member,int ... ids);
	
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
	

}
