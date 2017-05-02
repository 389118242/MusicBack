package com.back.music.dao.front;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.back.music.entity.front.SongCommentRelation;
import com.back.music.entity.front.pk.SongCommentPK;

public interface SongCommentDao extends CrudRepository<SongCommentRelation, SongCommentPK> {

	@Query(value = "select comId from song_com_relation where songId = :songId", nativeQuery = true)
	List<Integer> findComIdBySongId(@Param("songId") Integer songId);

	@Modifying
	@Transactional
	@Query(value = "delete from song_com_relation where songId = ?1", nativeQuery = true)
	int deleteRelationBySongId(Integer songId);

	@Modifying
	@Transactional
	@Query(value = "delete from song_com_relation where comId = ?1", nativeQuery = true)
	int deleteRelationByCommId(Integer commId);

	@Query(value = "SELECT comId FROM song_com_relation ORDER BY comId \n#pageable\n", countQuery = "SELECT count(comId) FROM song_com_relation", nativeQuery = true)
	Page<Integer> getSongCommentIds(Pageable pageable);

}
