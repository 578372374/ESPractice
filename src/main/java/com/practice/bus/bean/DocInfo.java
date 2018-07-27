package com.practice.bus.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 具体业务
 * 文档信息对应的bean
 *
 */
public class DocInfo{
	
	private Long docId;
	private String docName;
	private Integer docType;
	private String docStoreName;
	private String author;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private String docSummary;
	private Integer[] tags;
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Integer getDocType() {
		return docType;
	}
	public void setDocType(Integer docType) {
		this.docType = docType;
	}
	public String getDocStoreName() {
		return docStoreName;
	}
	public void setDocStoreName(String docStoreName) {
		this.docStoreName = docStoreName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDocSummary() {
		return docSummary;
	}
	public void setDocSummary(String docSummary) {
		this.docSummary = docSummary;
	}
	public Integer[] getTags() {
		return tags;
	}
	public void setTags(Integer[] tags) {
		this.tags = tags;
	}
}
