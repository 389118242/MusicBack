package com.back.music.dao.front;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.back.music.entity.front.SongListKindRelation;
import com.back.music.entity.front.pk.SongListKindPK;

public interface SongListKindDao extends CrudRepository<SongListKindRelation, SongListKindPK> {

	@Modifying
	@Transactional
	@Query(value = "delete from sl_lk_relation where lkId = ?1", nativeQuery = true)
	int deleteRelationByKindId(Integer kindId);

	@Modifying
	@Transactional
	@Query(value = "delete from sl_lk_relation where slId = ?1", nativeQuery = true)
	int deleteRelationByListId(Integer listId);

}
