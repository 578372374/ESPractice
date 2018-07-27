package com.practice.es.service;

import java.util.List;

/**
 * ES搜索条件对应的bean
 */
public class ESSearchReq {
	private String docId;
	private String author;
	private Integer docType;
	private List<Integer> tags;
	private int from = 0;
	private int size = 5;
	private String inputKeyword;
	private String beginTime;
	private String endTime;
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getDocType() {
		return docType;
	}
	public void setDocType(Integer docType) {
		this.docType = docType;
	}
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getInputKeyword() {
		return inputKeyword;
	}
	public void setInputKeyword(String inputKeyword) {
		this.inputKeyword = inputKeyword;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
