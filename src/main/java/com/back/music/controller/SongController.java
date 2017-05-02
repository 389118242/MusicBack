package com.back.music.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.back.music.dao.front.CommentDao;
import com.back.music.dao.front.RecommendSongDao;
import com.back.music.dao.front.ResourceDao;
import com.back.music.dao.front.SongCommentDao;
import com.back.music.dao.front.SongDao;
import com.back.music.entity.front.RecommendSong;
import com.back.music.entity.front.Resource;
import com.back.music.entity.front.Song;
import com.back.music.utils.MusicUtil;

@RestController
public class SongController {

	@Autowired
	private SongDao songDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private SongCommentDao songCommentDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private RecommendSongDao recommendSongDao;
	@Autowired
	private MusicUtil util;

	@RequestMapping(value = "addSong", method = RequestMethod.POST)
	public String addSong(MultipartFile lyricFile, MultipartFile songFile, Song song) {
		Resource resource = null;
		Integer songId = null;
		try {
			String songFileName = songFile.getOriginalFilename();
			resource = new Resource();
			resource.setData(songFile.getBytes());
			resource.setExtension(songFileName.substring(songFileName.lastIndexOf(".") + 1));
			resourceDao.save(resource);
			int resourceId = resource.getId();
			song.setResourceId(resourceId);
			if (null != lyricFile)
				song.setLyric(new String(lyricFile.getBytes(), "UTF-8"));
			song.setSongTime(getSongTime(songFile, resourceId));
			songDao.save(song);
			songId = song.getSongId();
		} catch (IOException e) {
			return "ERROR :　" + e.getMessage();
		}
		return null == songId ? "FAILED" : "SUCCESS";
	}

	private String getSongTime(MultipartFile songFile, int tag) {
		String time = null;
		String fileName = songFile.getOriginalFilename();
		File song = new File(System.getProperty("user.dir"), tag + "_" + fileName);
		try {
			songFile.transferTo(song);
			AudioFile audioFile = AudioFileIO.read(song);
			AudioHeader header = audioFile.getAudioHeader();
			int second = header.getTrackLength();
			int minute = second / 60;
			second = second % 60;
			time = formateNum(minute) + ":" + formateNum(second);
		} catch (CannotReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			e.printStackTrace();
		} finally {
			// song.deleteOnExit(); 由于流未关闭，无法用发方法删除~
			if (song.exists())
				while (!song.delete())
					;
		}
		return time;
	}

	private String formateNum(int num) {
		return (num > 9 ? "" : "0") + num;
	}

	@RequestMapping(value = "/getSongs", method = RequestMethod.POST)
	public Map<String, Object> getSongs(int page, int rows) {
		Page<Song> songs = songDao.findAll(new PageRequest(page - 1, rows));
		Map<String, Object> ret = new HashMap<>();
		ret.put("total", songs.getTotalElements());
		ret.put("rows", songs.getContent());
		return ret;
	}

	@RequestMapping(value = "updateSongFile", method = RequestMethod.POST)
	public String updateSongFile(MultipartFile file, int songId, Integer resourceId) {
		try {
			byte[] data = file.getBytes();
			String fileName = file.getOriginalFilename();
			Resource resource = new Resource();
			resource.setData(data);
			resource.setExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
			if (null != resourceId) {
				resource.setId(resourceId);
			}
			resource = resourceDao.save(resource);
			if (null == resourceId) {
				songDao.updateResource(songId, resource.getId());
			}
			songDao.updateSongTime(songId, getSongTime(file, resource.getId()));
			util.reloadFile("music", resource.getId());
		} catch (IOException e) {
			return "ERROR : " + e.getMessage();
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "updateLyric", method = RequestMethod.POST)
	public String updateLyric(MultipartFile file, int songId) {
		int affectRows = 0;
		try {
			String lyric = new String(file.getBytes(), "UTF-8");
			affectRows = songDao.updateLyric(songId, lyric);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Update Lyric Encoding Error : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Update Lyric Reading File Error : " + e.getMessage());
		}
		return 1 == affectRows ? "SUCCESS" : "FAILED";
	}

	@RequestMapping(value = "deleteSong", method = RequestMethod.POST)
	public String deleteSong(int songId) {
		Song song = songDao.getOne(songId);
		return util.deleteSong(song);
	}

	@RequestMapping(value = "getSongComments", method = RequestMethod.POST)
	public Map<String, Object> getSongComments(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<Integer> comIds = songCommentDao.getSongCommentIds(new PageRequest(page - 1, rows));
		ret.put("total", comIds.getTotalElements());
		ret.put("rows", commentDao.findByComIdIn(comIds.getContent()));
		return ret;
	}

	@RequestMapping(value = "getAllSongs", method = RequestMethod.POST)
	public List<Song> getAllSongs() {
		List<Integer> r_songIds = recommendSongDao.getRecommendSongIds();
		return r_songIds.size() == 0 ? songDao.findAll() : songDao.findAllBySongIdNotIn(r_songIds);
	}

	@RequestMapping(value = "getRecommendSongs", method = RequestMethod.POST)
	public List<Song> getRecommendSongs() {
		List<Song> songs = new ArrayList<>();
		List<RecommendSong> recommendSongs = recommendSongDao.findAll(new Sort("sort"));
		for (RecommendSong recommendSong : recommendSongs) {
			Integer songId = recommendSong.getSongId();
			Song song = songDao.findOne(songId);
			if (null != song)
				songs.add(song);
		}
		return songs;
	}

	@RequestMapping(value = "addRecommendSong", method = RequestMethod.POST)
	public String addRecommendSong(int songId, int sort) {
		RecommendSong recommendSong = new RecommendSong();
		recommendSong.setSongId(songId);
		recommendSong.setSort(sort);
		recommendSong = recommendSongDao.save(recommendSong);
		Integer r_id = recommendSong.getId();
		return null == r_id ? "FAILED" : "SUCCESS";
	}

	@RequestMapping(value = "removeRecommendSong", method = RequestMethod.POST)
	public String removeRecommendSong(int songId) {
		recommendSongDao.deleteRelationBySongId(songId);
		return "SUCCESS";
	}
}
