package com.back.music.entity.front;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.back.music.entity.front.pk.SongListSongPK;

@Entity(name = "sl_song_relation")
public class SongListSongRelation {

	@EmbeddedId
	private SongListSongPK id;

}
