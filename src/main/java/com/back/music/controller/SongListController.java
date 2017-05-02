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

import com.back.music.dao.front.CollectionDao;
import com.back.music.dao.front.CommentDao;
import com.back.music.dao.front.MessageDao;
import com.back.music.dao.front.SongListCommentDao;
import com.back.music.dao.front.SongListDao;
import com.back.music.dao.front.SongListKindDao;
import com.back.music.entity.front.Message;
import com.back.music.entity.front.SongList;
import com.back.music.utils.MusicUtil;

@RestController
public class SongListController {

	@Autowired
	private SongListDao songListDao;
	@Autowired
	private SongListCommentDao songListCommentDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private CollectionDao collectionDao;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private SongListKindDao songListKindDao;
	@Autowired
	private MusicUtil util;

	@RequestMapping(value = "/getSongLists", method = RequestMethod.POST)
	public Map<String, Object> getSongLists(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<SongList> songLists = songListDao.findAllByListNameNot("我喜欢的音乐", new PageRequest(page - 1, rows));
		ret.put("total", songLists.getTotalElements());
		for (SongList songList : songLists) {
			String img = songList.getListImg();
			songList.setListImg(util.getFrontBasePath() + img);
		}
		ret.put("rows", songLists.getContent());
		return ret;
	}

	@RequestMapping(value = "deleteSongList", method = RequestMethod.POST)
	public String deleteSongList(int listId, String why) {
		List<Integer> comIds = songListCommentDao.findComIdByListId(listId);
		songListCommentDao.deleteRelationByListId(listId);
		for (Integer comId : comIds) {
			commentDao.delete(comId);
		}
		collectionDao.deleteRelationByListId(listId);
		SongList list = songListDao.findOne(listId);
		Integer userId = list.getUser().getUserId();
		songListKindDao.deleteRelationByListId(listId);
		songListDao.delete(listId);
		Message mess = new Message();
		mess.setReceiveUserId(userId);
		mess.setSendUserId(userId);
		mess.setMessType("PRIVATE");
		mess.setMessContent("歌单被管理员删除<br>歌单名称：" + list.getListName() + "<br>删除理由：" + why);
		messageDao.save(mess);
		return "SUCCESS";
	}

	@RequestMapping(value = "getSongListComments", method = RequestMethod.POST)
	public Map<String, Object> getSongComments(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<Integer> comIds = songListCommentDao.getSongListCommentIds(new PageRequest(page - 1, rows));
		ret.put("total", comIds.getTotalElements());
		ret.put("rows", commentDao.findByComIdIn(comIds.getContent()));
		return ret;
	}

}
