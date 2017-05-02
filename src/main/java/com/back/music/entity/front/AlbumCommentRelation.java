package com.back.music.entity.front;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.back.music.entity.front.pk.AlbumCommentPK;

@Entity(name = "alb_com_relation")
public class AlbumCommentRelation {

	@EmbeddedId
	private AlbumCommentPK id;
}
