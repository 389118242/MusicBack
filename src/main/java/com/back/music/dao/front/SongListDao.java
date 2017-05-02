package com.back.music.dao.front;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.back.music.entity.front.SongList;

public interface SongListDao extends JpaRepository<SongList, Integer> {

	Page<SongList> findAllByListNameNot(String listName, Pageable pageable);

	List<SongList> findAllByListNameNot(String listName);

}
