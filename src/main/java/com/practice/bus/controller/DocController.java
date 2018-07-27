package com.practice.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.practice.bus.bean.DocInfo;
import com.practice.bus.service.DocService;

/**
 * 业务Controller
 * 使用for循环模拟大量请求，检测MQ和ES是否能正常工作
 */
@Controller
public class DocController {
	@Autowired
	DocService docService;
	
	/**
	 * 添加文档
	 */
	@RequestMapping("/createDoc")
	@ResponseBody
	public String createDoc(DocInfo docInfo) {
		for(long i =0;i<10;i++) {
			docInfo.setDocId(i);
			docService.createDoc(docInfo);
		}
		return "add success";
	}
	
	/**
	 * 修改文档
	 */
	@RequestMapping("/modifyDoc")
	@ResponseBody
	public String modifyDoc(DocInfo docInfo) {
		for(long i = 0;i<10;i++) {
			docInfo.setDocId(i);
			docService.modifyDoc(docInfo);
		}
		return "modify success";
	}
	
	/**
	 * 删除文档 
	 */
	@RequestMapping("/deleteDoc")
	@ResponseBody
	public String deleteDoc(DocInfo docInfo) {
		for(long i=0;i<10;i++) {
			docInfo.setDocId(i);
			docService.deleteDoc(docInfo);
		}
		return "delete success";
	}
}
