package com.back.music.dao.front;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.back.music.entity.front.RecommendSong;

public interface RecommendSongDao extends JpaRepository<RecommendSong, Integer> {

	@Modifying
	@Transactional
	@Query(value = "delete from recommend_song where songId = ?1", nativeQuery = true)
	int deleteRelationBySongId(Integer songId);

	@Query(value = "select songId from recommend_song order by sort", nativeQuery = true)
	List<Integer> getRecommendSongIds();
}
