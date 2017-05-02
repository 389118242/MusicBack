package com.back.music.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.back.music.dao.front.AlbumCommentDao;
import com.back.music.dao.front.AlbumDao;
import com.back.music.dao.front.CommentDao;
import com.back.music.dao.front.ResourceDao;
import com.back.music.dao.front.SingerDao;
import com.back.music.dao.front.SongCommentDao;
import com.back.music.dao.front.SongDao;
import com.back.music.dao.front.SongListSongDao;
import com.back.music.entity.front.Album;
import com.back.music.entity.front.Singer;
import com.back.music.entity.front.Song;

@Component
public class MusicUtil {

	@Autowired
	private SongDao songDao;
	@Autowired
	private SongListSongDao songListSongDao;
	@Autowired
	private SongCommentDao songCommentDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private AlbumDao albumDao;
	@Autowired
	private AlbumCommentDao albumCommentDao;
	@Autowired
	private SingerDao singerDao;

	@Value("${front.url}")
	private String frontUrl;
	private String frontBasePath;

	public byte[] getJPEGByte(MultipartFile file) {
		byte[] ret = null;
		try {
			BufferedImage srcImg = ImageIO.read(file.getInputStream());
			BufferedImage tagImg = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
			tagImg.getGraphics().drawImage(srcImg, 0, 0, 300, 300, null);
			ByteArrayOutputStream byteArrayOutputStrem = new ByteArrayOutputStream();
			ImageIO.write(tagImg, "JPEG", byteArrayOutputStrem);
			ret = byteArrayOutputStrem.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String deleteSong(Song song) {
		if (null == song)
			return "NOT_EXISTS";
		Integer songId = song.getSongId();
		songListSongDao.deleteRelation(songId);
		List<Integer> commIds = songCommentDao.findComIdBySongId(songId);
		songCommentDao.deleteRelationBySongId(songId);
		for (Integer id : commIds)
			commentDao.delete(id);
		Integer resourceId = song.getResourceId();
		if (null != resourceId)
			resourceDao.delete(resourceId);
		songDao.delete(songId);
		return "SUCCESS";
	}

	public String deleteAlbum(Album album) {
		if (null == album)
			return "NOT_EXISTS";
		Integer albumId = album.getAlbumId();
		List<Integer> commIds = albumCommentDao.findComIdByAlbumId(albumId);
		albumCommentDao.deleteRelationByAlbumId(albumId);
		for (Integer id : commIds)
			commentDao.delete(id);
		List<Song> songs = songDao.findByAlbumAlbumId(albumId);
		for (Song song : songs) {
			deleteSong(song);
		}
		Integer resourceId = album.getResourceId();
		if (null != resourceId)
			resourceDao.delete(resourceId);
		albumDao.delete(albumId);
		return "SUCCESS";
	}

	public String deleteSinger(Integer singerId) {
		Singer singer = singerDao.findOne(singerId);
		if (null == singer)
			return "NOT_EXISTS";
		List<Album> albums = albumDao.findBySingerSingerId(singerId);
		for (Album album : albums) {
			deleteAlbum(album);
		}
		Integer resourceId = singer.getResourceId();
		if (null != resourceId)
			resourceDao.delete(resourceId);
		singerDao.delete(singerId);
		return "SUCCESS";
	}

	/**
	 * 更新前台资源
	 * 
	 * @param type
	 *            [img|music]
	 * @param id
	 *            resourceId
	 *
	 */
	public void reloadFile(String type, Integer id) {
		try {
			URL url = new URL(frontUrl + "/reload");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3927);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(("type=" + type + "&id=" + id).getBytes());
			os.flush();
			InputStream is = conn.getInputStream();
			Reader reader = new InputStreamReader(is);
			char[] buffer = new char[9];
			StringBuffer string = new StringBuffer();
			int count = -1;
			while (-1 != (count = reader.read(buffer))) {
				string.append(buffer, 0, count);
			}
			System.out.println("File Reload (type:" + type + ",id:" + id + ") ret" + string.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFrontUrl() {
		return this.frontUrl;
	}

	public String getFrontBasePath() {
		return this.frontBasePath;
	}

	@PostConstruct
	public void formateFrontUrl() {
		if (!frontUrl.endsWith("/"))
			frontUrl += "/";
		int len = frontUrl.length();
		this.frontBasePath = frontUrl.substring(0, frontUrl.substring(0, len - 1).lastIndexOf("/") + 1);
	}

}
