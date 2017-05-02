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

import com.back.music.dao.front.AlbumDao;
import com.back.music.dao.front.RecordCompanyDao;
import com.back.music.entity.front.Album;
import com.back.music.entity.front.RecordCompany;
import com.back.music.utils.MusicUtil;

@RestController
public class RecordCompanyController {

	@Autowired
	private RecordCompanyDao recordCompanyDao;
	@Autowired
	private AlbumDao albumDao;
	@Autowired
	private MusicUtil util;

	@RequestMapping(value = "/getAllCompanys", method = RequestMethod.POST)
	public List<RecordCompany> getAllCompanys() {
		return recordCompanyDao.findAll();
	}

	@RequestMapping(value = "/getPagedCompanys", method = RequestMethod.POST)
	public Map<String, Object> getAllCompanys(int page, int rows) {
		Map<String, Object> ret = new HashMap<>();
		Page<RecordCompany> companys = recordCompanyDao.findAll(new PageRequest(page - 1, rows));
		ret.put("total", companys.getTotalElements());
		ret.put("rows", companys.getContent());
		return ret;
	}

	@RequestMapping(value = { "/addCompany", "/editCompany" }, method = RequestMethod.POST)
	public String addOredit(RecordCompany company) {
		recordCompanyDao.save(company);
		return null == company.getRcId() ? "FAILED" : "SUCCESS";
	}

	@RequestMapping(value = "removeCompany", method = RequestMethod.POST)
	public String remove(Integer rcId) {
		List<Album> albums = albumDao.findByCompanyRcId(rcId);
		for (Album album : albums) {
			util.deleteAlbum(album);
		}
		recordCompanyDao.delete(rcId);
		return "SUCCESS";
	}
}
