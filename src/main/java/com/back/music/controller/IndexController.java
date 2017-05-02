package com.back.music.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.back.music.dao.back.ManagerDao;
import com.back.music.entity.back.Manager;
import com.back.music.utils.MusicUtil;

@Controller
public class IndexController {

	@Autowired
	private ManagerDao manangerDao;
	@Autowired
	private MusicUtil util;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "redirect:/index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String showView(Model model) {
		model.addAttribute("frontUrl", util.getFrontUrl());
		return "index";
	}

	@RequestMapping(value = "/alterPassword", method = RequestMethod.POST)
	@ResponseBody
	public String alterPassword(String oldPass, String newPass, HttpSession session) {
		Manager loginManager = (Manager) session.getAttribute("loginManager");
		loginManager = manangerDao.login(loginManager.getAccount(), oldPass);
		if (null == loginManager)
			return "E";
		if (manangerDao.alterPassword(loginManager.getId(), newPass)) {
			session.removeAttribute("loginManager");
			return "S";
		} else {
			return "F";
		}
	}

}
