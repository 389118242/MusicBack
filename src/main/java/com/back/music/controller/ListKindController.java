package com.back.music.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.back.music.dao.front.ListKindDao;
import com.back.music.dao.front.SongListKindDao;
import com.back.music.entity.front.ListKind;

@RestController
public class ListKindController {

	@Autowired
	private ListKindDao listKindDao;
	@Autowired
	private SongListKindDao songListKindDao;

	@RequestMapping(value = "/getListKinds", method = RequestMethod.POST)
	public Map<String, Object> getListKinds(int page, int rows) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Page<ListKind> listKinds = listKindDao.findAll(new PageRequest(page - 1, rows));
		ret.put("total", listKinds.getTotalElements());
		ret.put("rows", listKinds.getContent());
		return ret;
	}

	@RequestMapping(value = "/addListKind", method = RequestMethod.POST)
	public String addListKind(String kindName) {
		ListKind listKind = new ListKind();
		listKind.setKindName(kindName);
		listKindDao.save(listKind);
		return null == listKind.getKindId() ? "FAILED" : "SUCCESS";
	}

	@RequestMapping(value = "/removeListKind", method = RequestMethod.POST)
	public String removeListKind(int kindId) {
		songListKindDao.deleteRelationByKindId(kindId);
		listKindDao.delete(kindId);
		return "SUCCESS";
	}
}
