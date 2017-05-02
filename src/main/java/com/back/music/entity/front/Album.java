package com.back.music.entity.front;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer albumId;

	@ManyToOne
	@JoinColumn(name = "singerId")
	private Singer singer;

	private String albumName;

	@ManyToOne
	@JoinColumn(name = "issueCompany")
	private RecordCompany company;

	@Temporal(TemporalType.DATE)
	private Date issueTime;

	private Integer resourceId;

	@Lob
	private String albumDetail;

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public String getAlbumDetail() {
		return albumDetail;
	}

	public void setAlbumDetail(String albumDetail) {
		this.albumDetail = albumDetail;
	}

	public Singer getSinger() {
		return singer;
	}

	public void setSinger(Singer singer) {
		this.singer = singer;
	}

	public RecordCompany getCompany() {
		return company;
	}

	public void setCompany(RecordCompany company) {
		this.company = company;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}