package com.back.music.entity.front;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.back.music.entity.front.pk.SongCommentPK;

@Entity(name = "song_com_relation")
public class SongCommentRelation {

	@EmbeddedId
	private SongCommentPK id;
}
