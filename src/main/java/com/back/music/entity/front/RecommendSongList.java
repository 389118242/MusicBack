package com.back.music.entity.front;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "recommend_song_list")
public class RecommendSongList {

	@Id
	private Integer index;

	private Integer slId;

	@Transient
	private SongList songList;

	private String img;

	private String detail;

	public SongList getSongList() {
		return songList;
	}

	public void setSongList(SongList songList) {
		this.songList = songList;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDetail() {
		return detail;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getSlId() {
		return slId;
	}

	public void setSlId(Integer slId) {
		this.slId = slId;
	}

}
