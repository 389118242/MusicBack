package com.back.music.entity.front;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.back.music.entity.front.pk.CollectionPK;

@Entity(name = "collection")
public class Collection {

	@EmbeddedId
	private CollectionPK id;
}
