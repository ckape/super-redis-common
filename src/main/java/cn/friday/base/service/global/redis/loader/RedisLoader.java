package cn.friday.base.service.global.redis.loader;

public abstract class RedisLoader<T> {

	public LoaderResult<T> call() {
		boolean isCache = false;
		int expireTime = 0;
		T t = load();
		if (t != null) {
			isCache = isNeedCache(t);
			expireTime = expire(t);
		}
		LoaderResult<T> result = new LoaderResult<T>(t, isCache);
		result.setExpireTime(expireTime);
		return result;
	}

	/**
	 * 是否需要缓存
	 * 
	 * @return 上午10:44:40
	 * 2016年5月6日
	 * @author jiangnan.zjn@alibaba-inc.com
	 */
	public abstract boolean isNeedCache(T t);

	public abstract T load();

	/**
	 * 设置过期时间，单位是秒
	 * 
	 * @param t
	 * @return 0=过期时间为1小时
	 * 2016年5月6日
	 * @author jiangnan.zjn@alibaba-inc.com
	 */
	public int expire(T t) {
		return 0;
	}
}
