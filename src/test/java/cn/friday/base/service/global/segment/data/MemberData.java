package cn.friday.base.service.global.segment.data;

public class MemberData {
	
	private int studentId;
	
	private int gender;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "MemberData [studentId=" + studentId + ", gender=" + gender
				+ "]";
	}
	
	

}
