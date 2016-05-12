package cn.friday.base.service.global.goods.model;

import java.util.Date;

public class GoodsRedis {

	private int id;

	private String title;

	private String name;

	private Date addTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Override
	public String toString() {
		return "GoodsRedis [id=" + id + ", title=" + title + ", name=" + name + ", addTime=" + addTime + "]";
	}

}
