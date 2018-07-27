package com.practice.es.service;

import java.util.Date;
import java.util.List;

/**
 * 与ES的Document对应的bean
 */
public class ESDocumentTemplate {
	/**
	 * 文档主键 docId type:long
	文件名 docName type:text
	作者 author type:keyword
	创建时间 createTime type:date format:yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
	文档类型(1:docx 2:pptx 3:xlsx) docType type:keyword
	文档摘要 docSummary type:text
	文档标签 tag(1:技术文档 2:纪律规范 3:对外宣传 4:信息记录 5:交流培训 6:新员工必读 7:管理必看) type:text （数组）
	 */
	private String docId;
	private String author;
	private String docName;
	private Date createTime;
	private Integer docType;
	private String docSummary;
	private List<Integer> tags;
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
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getDocType() {
		return docType;
	}
	public void setDocType(Integer docType) {
		this.docType = docType;
	}
	public String getDocSummary() {
		return docSummary;
	}
	public void setDocSummary(String docSummary) {
		this.docSummary = docSummary;
	}
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
}
