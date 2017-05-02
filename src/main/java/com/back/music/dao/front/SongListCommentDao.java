package com.back.music.dao.front;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.back.music.entity.front.SongListCommentRelation;
import com.back.music.entity.front.pk.SongCommentPK;

public interface SongListCommentDao extends CrudRepository<SongListCommentRelation, SongCommentPK> {

	@Query(value = "select comId from sl_com_relation where slId = :slId", nativeQuery = true)
	List<Integer> findComIdByListId(@Param("slId") Integer slId);

	@Modifying
	@Transactional
	@Query(value = "delete from sl_com_relation where slId = ?1", nativeQuery = true)
	int deleteRelationByListId(Integer slId);

	@Modifying
	@Transactional
	@Query(value = "delete from sl_com_relation where comId = ?1", nativeQuery = true)
	int deleteRelationByCommId(Integer commId);

	@Query(value = "SELECT comId FROM sl_com_relation ORDER BY comId \n#pageable\n", countQuery = "SELECT count(comId) FROM sl_com_relation", nativeQuery = true)
	Page<Integer> getSongListCommentIds(Pageable pageable);

}
