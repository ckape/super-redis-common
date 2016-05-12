package cn.friday.base.service.global.redis.localcache;

public interface LocalCache<K, V> {

	V get(String... seqs);

	void put(V v, String... seqs);

}
