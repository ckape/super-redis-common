package cn.friday.base.service.global.redis.loader;

public class LoaderResult<V> {

	private V v;

	private boolean isCache;

	//过期时间(秒)
	private int expireTime;

	//默认缓存一个小时
	private final static int DEFAULT_EXPIRE_TIME = 3600;

	public LoaderResult(V v, boolean isCache) {
		this.v = v;
		this.isCache = isCache;
	}

	public V getV() {
		return v;
	}

	public void setV(V v) {
		this.v = v;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}

	public int getExpireTime() {
		if (expireTime == 0) {
			return DEFAULT_EXPIRE_TIME;
		}
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "LoaderResult [v=" + v + ", isCache=" + isCache + ", expireTime=" + expireTime + "]";
	}

}
