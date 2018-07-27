package com.practice.es.service;

import java.util.List;

/**
 *ES搜索结果对应的bean 
 *
 */
public class ESSearchResp<T> {
    private long total;
    private List<T> result;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public ESSearchResp() {
		super();
	}
	public ESSearchResp(long total, List<T> result) {
		super();
		this.total = total;
		this.result = result;
	}	
}
