package com.back.music.entity.front.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.back.music.entity.front.Comment;
import com.back.music.entity.front.Song;

@Embeddable
@SuppressWarnings("serial")
public class SongCommentPK implements Serializable {

	@ManyToOne
	@JoinColumn(name = "songId")
	private Song song;

	@OneToOne
	@JoinColumn(name = "comId")
	private Comment comm;

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public Comment getComm() {
		return comm;
	}

	public void setComm(Comment comm) {
		this.comm = comm;
	}

}
