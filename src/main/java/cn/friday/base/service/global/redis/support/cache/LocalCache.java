package cn.friday.base.service.global.redis.support.cache;

public interface LocalCache<K, V> {

	V get(String... seqs);

	void put(V v, String... seqs);

}
