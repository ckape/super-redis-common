package cn.friday.base.service.global.test;

public class Data {
	
	private int id;
	
	private String member;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", member=" + member + "]";
	}
	
	

}
