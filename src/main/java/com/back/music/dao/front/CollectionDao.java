package com.back.music.dao.front;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.back.music.entity.front.Collection;
import com.back.music.entity.front.pk.CollectionPK;

public interface CollectionDao extends CrudRepository<Collection, CollectionPK> {

	@Modifying
	@Transactional
	@Query(value = "delete from collection where listId = ?1", nativeQuery = true)
	int deleteRelationByListId(Integer slId);

}
