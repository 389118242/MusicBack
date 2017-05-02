package com.back.music.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.front.RecommendSongListDao;
import com.back.music.dao.front.SongListDao;
import com.back.music.entity.front.RecommendSongList;
import com.back.music.entity.front.SongList;
import com.back.music.utils.MusicUtil;

@RestController
public class RecommendSongListController {

	@Autowired
	private RecommendSongListDao recommendSongListDao;
	@Autowired
	private SongListDao songListDao;
	@Autowired
	private MusicUtil util;

	@RequestMapping(value = "/getRecommendSongList", method = RequestMethod.POST)
	public Iterable<RecommendSongList> getRecommendSongList() {
		Iterable<RecommendSongList> rsls = recommendSongListDao.findAll();
		for (RecommendSongList rsl : rsls) {
			Integer listId = rsl.getSlId();
			if (null != listId) {
				SongList songList = songListDao.findOne(listId);
				rsl.setSongList(songList);
			}
			String img = rsl.getImg();
			if (null != img) {
				rsl.setImg(util.getFrontBasePath() + img.substring(1));
			}
		}
		return rsls;
	}

	@RequestMapping(value = "/getAllSongList", method = RequestMethod.POST)
	public List<SongList> getAllSongList() {
		return songListDao.findAllByListNameNot("我喜欢的音乐");
	}

}
