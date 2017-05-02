package com.back.music.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.back.music.dao.front.ResourceDao;
import com.back.music.dao.front.SingerDao;
import com.back.music.entity.front.Resource;
import com.back.music.entity.front.Singer;
import com.back.music.utils.MusicUtil;

@RestController
public class SingerController {

	@Autowired
	private SingerDao singerDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private MusicUtil dbUtil;

	@RequestMapping(value = "/getSingers", method = RequestMethod.POST)
	public List<Singer> getSingers() {
		return singerDao.findAll();
	}

	@RequestMapping(value = "/getPagedSingers", method = RequestMethod.POST)
	public Map<String, Object> getPagedSingers(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<Singer> singers = singerDao.findAll(new PageRequest(page - 1, rows));
		ret.put("total", singers.getTotalElements());
		ret.put("rows", singers.getContent());
		return ret;
	}

	@RequestMapping(value = "/addSinger", method = RequestMethod.POST)
	public String addSinger(MultipartFile file, String name, String detail) {
		byte[] imgData = dbUtil.getJPEGByte(file);
		if (null == imgData)
			return "get image data throw error.";
		Resource resource = new Resource();
		resource.setData(imgData);
		resource.setExtension("jpg");
		resourceDao.save(resource);
		Integer rId = resource.getId();
		if (null == rId)
			return "save image data error";
		Singer singer = new Singer();
		singer.setResourceId(rId);
		singer.setSingerName(name);
		singer.setSingerDetail(detail);
		singerDao.save(singer);
		return null == singer.getSingerId() ? "FAILED" : "SUCCESS";
	}

	@RequestMapping(value = "/editSinger", method = RequestMethod.POST)
	public String editSinger(MultipartFile file, Integer id, String name, String detail) {
		Singer singer = singerDao.findOne(id);
		singer.setSingerDetail(detail);
		singer.setSingerName(name);
		if (null != file) {
			byte[] imgData = dbUtil.getJPEGByte(file);
			if (null == imgData)
				return "get image data throw error.";
			Resource resource = new Resource();
			Integer rId = singer.getResourceId();
			if (null != rId)
				resource.setId(rId);
			resource.setData(imgData);
			resource.setExtension("jpg");
			resourceDao.save(resource);
			singer.setResourceId(resource.getId());
			dbUtil.reloadFile("img", resource.getId());
		}
		singerDao.save(singer);
		return "SUCCESS";
	}

	@RequestMapping(value = "/removeSinger", method = RequestMethod.POST)
	public String removeSinger(int singerId) {
		return dbUtil.deleteSinger(singerId);
	}

}
