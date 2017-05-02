package com.back.music.entity.front.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.back.music.entity.front.Album;
import com.back.music.entity.front.Comment;

@Embeddable
@SuppressWarnings("serial")
public class AlbumCommentPK implements Serializable {

	@ManyToOne
	@JoinColumn(name = "albId")
	private Album album;

	@OneToOne
	@JoinColumn(name = "comId")
	private Comment comm;

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Comment getComm() {
		return comm;
	}

	public void setComm(Comment comm) {
		this.comm = comm;
	}

}
