package com.back.music.dao.front;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.music.entity.front.Singer;

public interface SingerDao extends JpaRepository<Singer, Integer> {

}
