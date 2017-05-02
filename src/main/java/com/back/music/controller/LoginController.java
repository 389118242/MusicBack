package com.back.music.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.back.music.dao.back.ManagerDao;
import com.back.music.entity.back.Manager;

@Controller
public class LoginController {

	@Autowired
	private ManagerDao manangerDao;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showView(Model model) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String account, String password, HttpSession session, RedirectAttributes model) {
		String path = "index";
		Manager manager = manangerDao.login(account, password);
		if (manager == null) {
			model.addFlashAttribute("error", "ERROR");
			path = "login";
		} else {
			session.setAttribute("loginManager", manager);
		}
		return "redirect:/" + path;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.removeAttribute("loginManager");
		return "redirect:/login";
	}

}
