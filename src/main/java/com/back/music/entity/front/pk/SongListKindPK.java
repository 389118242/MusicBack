package com.back.music.entity.front.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.back.music.entity.front.ListKind;
import com.back.music.entity.front.SongList;

@Embeddable
@SuppressWarnings("serial")
public class SongListKindPK implements Serializable {

	@ManyToOne
	@JoinColumn(name = "slId")
	private SongList songList;

	@ManyToOne
	@JoinColumn(name = "lkId")
	private ListKind listKind;

}
