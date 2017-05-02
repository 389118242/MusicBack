package com.back.music.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.front.AlbumCommentDao;
import com.back.music.dao.front.CommentDao;
import com.back.music.dao.front.MessageDao;
import com.back.music.dao.front.SongCommentDao;
import com.back.music.dao.front.SongListCommentDao;
import com.back.music.entity.front.Comment;
import com.back.music.entity.front.Message;

@RestController
public class CommentController {

	@Autowired
	private SongCommentDao songCommentDao;
	@Autowired
	private SongListCommentDao songListCommentDao;
	@Autowired
	private AlbumCommentDao albumCommentDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private MessageDao messageDao;

	@RequestMapping(value = "deleteComment", method = RequestMethod.POST)
	public String deleteComment(String type, int comId, String why) {
		String tag = null;
		if ("song".equals(type)) {
			songCommentDao.deleteRelationByCommId(comId);
			tag = "歌曲评论";
		} else if ("songList".equals(type)) {
			songListCommentDao.deleteRelationByCommId(comId);
			tag = "歌单评论";
		} else if ("album".equals(type)) {
			albumCommentDao.deleteRelationByCommId(comId);
			tag = "专辑评论";
		}
		Comment comment = commentDao.findOne(comId);
		Message message = new Message();
		Integer userId = comment.getUser().getUserId();
		message.setSendUserId(userId);
		message.setReceiveUserId(userId);
		message.setMessType("PRIVATE");
		message.setMessContent(tag + "被管理员删除<br>评论内容：" + comment.getComContent() + "<br>删除理由：" + why);
		commentDao.delete(comId);
		messageDao.save(message);
		return "SUCCESS";
	}

}
