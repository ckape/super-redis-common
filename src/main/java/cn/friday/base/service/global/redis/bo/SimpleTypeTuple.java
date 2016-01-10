package cn.friday.base.service.global.redis.bo;

public class SimpleTypeTuple<T> {

	private T value;
	
	private Double score;

	public SimpleTypeTuple(T value, Double score) {
		super();
		this.value = value;
		this.score = score;
	}

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
		return "SimpleTypeTuple [value=" + value + ", score=" + score + "]";
	}
	
	
}
