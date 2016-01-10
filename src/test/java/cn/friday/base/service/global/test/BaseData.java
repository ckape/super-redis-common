package cn.friday.base.service.global.test;

public class BaseData extends Data{
	
	private String name;
	
	private int age;
	
	private boolean isAny;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	
	public boolean isAny() {
		return isAny;
	}

	public void setAny(boolean isAny) {
		this.isAny = isAny;
	}

	@Override
	public String toString() {
		return "BaseData [name=" + name + ", age=" + age + ", isAny=" + isAny
				+ "]";
	}
	
	

}
