package com.back.music.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.front.ResourceDao;
import com.back.music.entity.front.Resource;

@RestController
public class ResourceController {

	@Autowired
	private ResourceDao resourceDao;

	@RequestMapping(value = "img/{resourceId}", method = RequestMethod.GET)
	public void getImg(@PathVariable int resourceId, HttpServletResponse response) {
		response.setContentType("image/jpeg");
		try {
			Resource resource = resourceDao.findOne(resourceId);
			OutputStream os = response.getOutputStream();
			os.write(resource.getData());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
