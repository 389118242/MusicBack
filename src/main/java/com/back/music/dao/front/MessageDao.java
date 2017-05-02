package com.back.music.dao.front;

import org.springframework.data.repository.CrudRepository;

import com.back.music.entity.front.Message;

public interface MessageDao extends CrudRepository<Message, Integer> {

}
