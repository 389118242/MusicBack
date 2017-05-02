package com.back.music.entity.front;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.back.music.entity.front.pk.SongListKindPK;

@Entity(name = "sl_lk_relation")
public class SongListKindRelation {

	@EmbeddedId
	private SongListKindPK id;
}
