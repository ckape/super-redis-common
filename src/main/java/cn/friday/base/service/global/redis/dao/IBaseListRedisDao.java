package cn.friday.base.service.global.redis.dao;

public interface IBaseListRedisDao {
	
	/**
	 * 从左边入队列一个元素
	 * @param member
	 * @param ids
	 * @return
	 */
	public long lpush(String member,int... ids);
	
	/**
	 * 从右边出队列
	 * @param ids
	 * @return
	 */
	public String rpop(int... ids);
	
	/**
	 * 获取长度
	 * @param ids
	 * @return
	 */
	public long size(int... ids);

}
