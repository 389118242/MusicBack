package com.back.music.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.front.MessageDao;
import com.back.music.dao.front.UserDao;
import com.back.music.entity.front.Message;
import com.back.music.entity.front.User;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private MessageDao messageDao;

	@RequestMapping(value = "/getUsers", method = RequestMethod.POST)
	public Map<String, Object> getUsers(int page, int rows) {
		Page<User> users = userDao.findAll(new PageRequest(page - 1, rows));
		Map<String, Object> ret = new HashMap<>();
		ret.put("total", users.getTotalElements());
		ret.put("rows", users.getContent());
		return ret;
	}

	@RequestMapping(value = "/changeState", method = RequestMethod.POST)
	public boolean changeState(int userId, int userState) {
		return 1 == userDao.chageState(userId, userState);
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public boolean sendMessage(int userId, String mess) {
		Message message = new Message();
		message.setReceiveUserId(userId);
		message.setSendUserId(userId);
		message.setMessType("PRIVATE");
		message.setMessContent(mess);
		messageDao.save(message);
		return null != message.getMessId();
	}

}
