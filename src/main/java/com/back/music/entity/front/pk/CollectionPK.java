package com.back.music.entity.front.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.back.music.entity.front.SongList;
import com.back.music.entity.front.User;

@Embeddable
@SuppressWarnings("serial")
public class CollectionPK implements Serializable {
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	@ManyToOne
	@JoinColumn(name = "listId")
	private SongList songList;
}
