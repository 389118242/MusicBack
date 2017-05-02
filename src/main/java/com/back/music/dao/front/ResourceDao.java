package com.back.music.dao.front;

import org.springframework.data.repository.CrudRepository;

import com.back.music.entity.front.Resource;

public interface ResourceDao extends CrudRepository<Resource, Integer> {

}
