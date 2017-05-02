package com.back.music.dao.front;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.back.music.entity.front.Comment;

public interface CommentDao extends JpaRepository<Comment, Integer> {

	List<Comment> findByComIdIn(List<Integer> comIds);
	
}
