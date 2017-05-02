package com.back.music.dao.front;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.back.music.entity.front.AlbumCommentRelation;
import com.back.music.entity.front.pk.AlbumCommentPK;

public interface AlbumCommentDao extends CrudRepository<AlbumCommentRelation, AlbumCommentPK> {

	@Query(value = "select comId from alb_com_relation where albId = :albId", nativeQuery = true)
	List<Integer> findComIdByAlbumId(@Param("albId") Integer albId);

	@Modifying
	@Transactional
	@Query(value = "delete from alb_com_relation where albId = ?1", nativeQuery = true)
	int deleteRelationByAlbumId(Integer albumId);

	@Modifying
	@Transactional
	@Query(value = "delete from alb_com_relation where comId = ?1", nativeQuery = true)
	int deleteRelationByCommId(Integer commId);

	@Query(value = "SELECT comId FROM alb_com_relation ORDER BY comId \n#pageable\n", countQuery = "SELECT count(comId) FROM alb_com_relation", nativeQuery = true)
	Page<Integer> getAlbumCommentIds(Pageable pageable);

}
