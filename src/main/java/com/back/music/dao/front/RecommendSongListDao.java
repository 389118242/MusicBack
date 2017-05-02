package com.back.music.dao.front;

import org.springframework.data.repository.CrudRepository;

import com.back.music.entity.front.RecommendSongList;

public interface RecommendSongListDao extends CrudRepository<RecommendSongList, Integer> {

}
