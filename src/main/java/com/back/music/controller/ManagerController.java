package com.back.music.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.back.ManagerDao;
import com.back.music.entity.back.Manager;

@RestController
public class ManagerController {

	@Autowired
	private ManagerDao managerDao;

	@RequestMapping(value = "/getManagers", method = RequestMethod.POST)
	public List<Manager> getManagers() {
		return managerDao.getAllManager();
	}

	@RequestMapping(value = "/addManager", method = RequestMethod.POST)
	public String addManager(String account, String name, String pass) {
		if (managerDao.exists(account))
			return "EXISTS";
		if (managerDao.addManager(account, name, pass))
			return "SUCCESS";
		else
			return "FAILED";
	}

	@RequestMapping(value = "/removeManager", method = RequestMethod.POST)
	public boolean removeManger(int id) {
		return managerDao.removeManager(id);
	}
}
