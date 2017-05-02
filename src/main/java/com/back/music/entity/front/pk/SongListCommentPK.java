package com.back.music.entity.front.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.back.music.entity.front.Comment;
import com.back.music.entity.front.SongList;

@Embeddable
@SuppressWarnings("serial")
public class SongListCommentPK implements Serializable {

	@ManyToOne
	@JoinColumn(name = "slId")
	private SongList songList;

	@OneToOne
	@JoinColumn(name = "comId")
	private Comment comm;

	public SongList getSongList() {
		return songList;
	}

	public void setSongList(SongList songList) {
		this.songList = songList;
	}

	public Comment getComm() {
		return comm;
	}

	public void setComm(Comment comm) {
		this.comm = comm;
	}

}
