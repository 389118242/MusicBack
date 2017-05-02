package com.back.music.entity.front;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.back.music.entity.front.pk.SongListCommentPK;

@Entity(name = "sl_com_relation")
public class SongListCommentRelation {

	@EmbeddedId
	private SongListCommentPK id;
}
