package com.back.music.dao.front;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.back.music.entity.front.User;

public interface UserDao extends JpaRepository<User, Integer> {

	@Modifying
	@Transactional
	@Query("update User u set u.userState = ?2 where u.userId = ?1")
	int chageState(int id, int state);

}
