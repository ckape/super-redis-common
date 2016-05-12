package cn.friday.base.service.global.redis.support.cache;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 本地缓存抽象实现
 * @author BravoZu
 *
 * @param <K>
 * @param <V>
 */
public abstract class AbstractLocalCache<V> implements LocalCache<String, V> {

	LoadingCache<String, V> cache;

	String baseKey;

	/**
	 * 默认本地缓存最大长度
	 */
	private final static int DEFAULT_MAX_CAHCE_SIZE = 200;

	/**
	 * 默认缓存的时间
	 */
	private final static int DEFAULT_CAHCE_SECOND = 10;

	public AbstractLocalCache(String baseKey) {
		this(baseKey, DEFAULT_MAX_CAHCE_SIZE, DEFAULT_CAHCE_SECOND);
	}

	public AbstractLocalCache(String baseKey, int second) {
		this(baseKey, DEFAULT_MAX_CAHCE_SIZE, second);
	}

	/**
	 * 
	 * @param baseKey
	 * @param size： size = -1时长度不限制
	 * @param second：second = -1永不过期
	 */
	public AbstractLocalCache(String baseKey, int size, int second) {
		this.baseKey = new StringBuffer().append(baseKey).append(".local").append(":{0}").toString();
		init(size, second);
	}

	private void init(int size, int second) {
		if (size == -1 && second == -1) {
			cache = CacheBuilder.newBuilder().build(new CacheLoader<String, V>() {
				@Override
				public V load(String key) throws Exception {
					return reloadData(key);
				}
			});
		} else if (size == -1) {
			cache = CacheBuilder.newBuilder().expireAfterWrite(second, TimeUnit.SECONDS)
					.build(new CacheLoader<String, V>() {
						@Override
						public V load(String key) throws Exception {
							return reloadData(key);
						}
					});
		} else if (second == -1) {
			cache = CacheBuilder.newBuilder().maximumSize(size).build(new CacheLoader<String, V>() {
				@Override
				public V load(String key) throws Exception {
					return reloadData(key);
				}
			});
		} else {
			cache = CacheBuilder.newBuilder().maximumSize(size).expireAfterWrite(second, TimeUnit.SECONDS)
					.build(new CacheLoader<String, V>() {
						@Override
						public V load(String key) throws Exception {
							return reloadData(key);
						}
					});
		}
	}

	@Override
	public V get(String... seqs) {
		try {
			return cache.get(buildKey(seqs));
		} catch (Exception e) {
			//			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void put(V v, String... seqs) {
		cache.put(buildKey(seqs), v);
	}

	private String buildKey(String... ids) {
		if (ids.length == 0) {
			return MessageFormat.format(baseKey, "");
		} else {
			StringBuilder builder = new StringBuilder();
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < ids.length; i++) {
				if (!Strings.isNullOrEmpty(ids[i])) {
					list.add(ids[i]);
				}
			}
			if (list.size() == 0) {
				return MessageFormat.format(baseKey, "");
			} else {
				Joiner.on(":").appendTo(builder, list);
				return MessageFormat.format(baseKey, builder.toString());
			}
		}
	}

	private V reloadData(String key) {
		List<String> result = Splitter.on(":").omitEmptyStrings().splitToList(key);
		if (result.size() > 1) {
			result = result.subList(1, result.size());
		}
		return reloadData(result);
	}

	public abstract V reloadData(List<String> ids);

}
