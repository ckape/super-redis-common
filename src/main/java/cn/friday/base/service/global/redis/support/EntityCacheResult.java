package cn.friday.base.service.global.redis.support;

import java.util.Map;
import java.util.Set;

/**
 * 实体缓存的查询结果集
 * @author BravoZu
 *
 * @param <T>
 */
public class EntityCacheResult<T> {

	private Set<Long> ids;
	
	private Map<Long, T> result;

	public EntityCacheResult() {
		super();
	}

	public EntityCacheResult(Set<Long> ids, Map<Long, T> result) {
		super();
		this.ids = ids;
		this.result = result;
	}

	public Set<Long> getIds() {
		return ids;
	}

	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}

	public Map<Long, T> getResult() {
		return result;
	}

	public void setResult(Map<Long, T> result) {
		this.result = result;
	}
	
}
