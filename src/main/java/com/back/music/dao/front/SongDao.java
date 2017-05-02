package com.back.music.dao.front;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.back.music.entity.front.Song;

public interface SongDao extends JpaRepository<Song, Integer> {

	@Modifying
	@Transactional
	@Query("update Song s set s.resourceId = ?2 where s.songId = ?1")
	int updateResource(int id, int resourceId);

	@Modifying
	@Transactional
	@Query("update Song s set s.songTime = :time where s.songId = :id")
	int updateSongTime(@Param("id") int id, @Param("time") String songTime);

	@Modifying
	@Transactional
	@Query("update Song s set s.lyric = ?2 where s.songId = ?1")
	int updateLyric(int id, String lyric);

	List<Song> findAllBySongIdNotIn(List<Integer> songIds);

	List<Song> findByAlbumAlbumId(Integer albumId);

}
