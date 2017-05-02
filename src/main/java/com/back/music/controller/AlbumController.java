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

import com.back.music.dao.front.AlbumCommentDao;
import com.back.music.dao.front.AlbumDao;
import com.back.music.dao.front.CommentDao;
import com.back.music.dao.front.ResourceDao;
import com.back.music.entity.front.Album;
import com.back.music.entity.front.Resource;
import com.back.music.utils.MusicUtil;

@RestController
public class AlbumController {

	@Autowired
	private AlbumDao albumDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private MusicUtil util;
	@Autowired
	private AlbumCommentDao albumCommentDao;
	@Autowired
	private CommentDao commentDao;

	@RequestMapping(value = "getAlbumBySingerId", method = RequestMethod.POST)
	public List<Album> getAlbumBy(int singerId) {
		return albumDao.findBySingerSingerId(singerId);
	}

	@RequestMapping(value = "getAllAlbums", method = RequestMethod.POST)
	public Map<String, Object> getAllAlbums(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<Album> albums = albumDao.findAll(new PageRequest(page - 1, rows));
		ret.put("total", albums.getTotalElements());
		ret.put("rows", albums.getContent());
		return ret;
	}

	@RequestMapping(value = "addAlbum", method = RequestMethod.POST)
	public String addAlbum(MultipartFile file, Album album) {
		byte[] imgData = util.getJPEGByte(file);
		if (null == imgData)
			return "Get Image Data Error";
		Resource resource = new Resource();
		resource.setData(imgData);
		resource.setExtension("jpg");
		resourceDao.save(resource);
		Integer rId = resource.getId();
		if (null == rId)
			return "Save Image Data Error";
		album.setResourceId(rId);
		albumDao.save(album);
		return null == album.getAlbumId() ? "FAILED" : "SUCCESS";
	}

	@RequestMapping(value = "/editAlbum", method = RequestMethod.POST)
	public String editAlbum(MultipartFile file, Album album) {
		if (null != file) {
			byte[] imgData = util.getJPEGByte(file);
			if (null == imgData)
				return "Get Image Data Error";
			Resource resource = new Resource();
			Integer rId = album.getResourceId();
			if (null != rId)
				resource.setId(rId);
			resource.setData(imgData);
			resource.setExtension("jpg");
			resourceDao.save(resource);
			album.setResourceId(resource.getId());
			util.reloadFile("img", resource.getId());
		}
		albumDao.save(album);
		return "SUCCESS";
	}

	@RequestMapping(value = "/removeAlbum", method = RequestMethod.POST)
	public String removeAlbum(int albumId) {
		Album album = albumDao.findOne(albumId);
		return util.deleteAlbum(album);
	}

	@RequestMapping(value = "getAlbumComments", method = RequestMethod.POST)
	public Map<String, Object> getSongComments(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<Integer> comIds = albumCommentDao.getAlbumCommentIds(new PageRequest(page - 1, rows));
		ret.put("total", comIds.getTotalElements());
		ret.put("rows", commentDao.findByComIdIn(comIds.getContent()));
		return ret;
	}
}
