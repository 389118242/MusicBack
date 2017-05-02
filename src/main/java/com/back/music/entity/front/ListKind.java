package com.back.music.entity.front;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "list_kind")
public class ListKind {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer kindId;

	private String kindName;

	public Integer getKindId() {
		return kindId;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
}