package com.back.music.dao.front;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.music.entity.front.Album;

public interface AlbumDao extends JpaRepository<Album, Integer> {

	List<Album> findBySingerSingerId(Integer singerId);

	List<Album> findByCompanyRcId(Integer rcId);
}
