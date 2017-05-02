package com.back.music.controller;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.front.CommentDao;
import com.back.music.dao.front.RecordCompanyDao;
import com.back.music.dao.front.UserDao;
import com.back.music.entity.front.Comment;
import com.back.music.entity.front.RecordCompany;
import com.back.music.entity.front.User;

@RestController
public class TestController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RecordCompanyDao rcDao;
	@Autowired
	private CommentDao commentDao;

	@RequestMapping(value = "/all-user")
	public Iterator<User> getAllUser() {
		return userDao.findAll().iterator();
	}

	@RequestMapping(value = "/all-com")
	public Iterator<RecordCompany> getAllCompany() {
		return rcDao.findAll().iterator();
	}

	@RequestMapping(value = "/all-comm")
	public Iterator<Comment> getAllComm() {
		Iterator<Comment> comms = commentDao.findAll().iterator();
		// Comment comm = comms.next();
		// System.out.println(comm);
		return comms;
	}

	@RequestMapping(value = "/add-comm")
	public Comment addComm() {
		return null;
	}

}
