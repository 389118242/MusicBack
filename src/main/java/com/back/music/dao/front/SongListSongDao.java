package com.back.music.dao.front;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.back.music.entity.front.SongListSongRelation;
import com.back.music.entity.front.pk.SongListSongPK;

public interface SongListSongDao extends CrudRepository<SongListSongRelation, SongListSongPK> {

	@Modifying
	@Transactional
	@Query(value = "delete from sl_song_relation where songId = ?1", nativeQuery = true)
	int deleteRelation(Integer songId);

}
