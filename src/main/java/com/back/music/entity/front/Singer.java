package com.back.music.entity.front;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Singer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer singerId;

	private String singerName;

	private Integer resourceId;

	@Lob
	private String singerDetail;

	public Integer getSingerId() {
		return singerId;
	}

	public void setSingerId(Integer singerId) {
		this.singerId = singerId;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getSingerDetail() {
		return singerDetail;
	}

	public void setSingerDetail(String singerDetail) {
		this.singerDetail = singerDetail;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}