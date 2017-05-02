package com.back.music.entity.front.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.back.music.entity.front.Song;
import com.back.music.entity.front.SongList;

@Embeddable
@SuppressWarnings("serial")
public class SongListSongPK implements Serializable {

	@ManyToOne
	@JoinColumn(name = "slId")
	private SongList songList;
	@ManyToOne
	@JoinColumn(name = "songId")
	private Song song;

	public SongList getSongList() {
		return songList;
	}

	public void setSongList(SongList songList) {
		this.songList = songList;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}
}
