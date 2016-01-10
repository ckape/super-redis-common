package cn.friday.base.service.global.redis.bo;
/**
 * zset返回的结果解析
 * @author Zz
 *
 */
public class ZsetResult<T> {
	
	public ZsetResult(T value, Double score) {
		super();
		this.value = value;
		this.score = score;
	}

	private T value;
	
	private Double score;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ZsetResult [value=" + value + ", score=" + score + "]";
	}

	

}
