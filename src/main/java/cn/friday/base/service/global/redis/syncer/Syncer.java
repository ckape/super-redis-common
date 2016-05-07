package cn.friday.base.service.global.redis.syncer;

/**
 * <b><code>Syncer</code></b>
 * <p>
 * 同步器接口
 * </p>
 */
public interface Syncer<T> {

	public void excute(T t);

}
