package com.practice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.bus.bean.DocInfo;
import com.practice.es.service.ESDocumentTemplate;
import com.practice.es.service.ESSearchReq;
import com.practice.es.service.ESSearchResp;
import com.practice.es.service.ESService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {
	@Autowired
	ESService esService;
	
	@Autowired
	TransportClient transportClient;
	
	@Autowired
	ObjectMapper mapper;
	
	@Test
	public void contextLoads() {
	}

	/*****************************测试ES文档的增删改查*********************/
	@Test
	public void testCreateDocument() throws ParseException {
		ESDocumentTemplate documentTemplate = new ESDocumentTemplate();
		documentTemplate.setDocId("1");
		documentTemplate.setAuthor("张三");
		documentTemplate.setDocName("网上商城需求文档.docx");
		documentTemplate.setDocSummary("网上商城系统主要技术框架SpringBoot+ElasticSearch，为用户提供类似淘宝的一站式购物体验！");
		documentTemplate.setDocType(1);
		documentTemplate.setTags(Arrays.asList(new Integer[]{1,6}));
		documentTemplate.setCreateTime(new Date());
		
		boolean result = esService.createDocument(documentTemplate);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testUpdateDocument() throws ParseException {
		ESDocumentTemplate documentTemplate = new ESDocumentTemplate();
		documentTemplate.setDocId("1");
		documentTemplate.setAuthor("张三");
		documentTemplate.setDocName("网上商城需求文档.docx");
		documentTemplate.setDocSummary("网上商城系统主要技术框架SpringBoot+ElasticSearch，为用户提供类似淘宝的一站式购物体验，欢迎来使用！");
		documentTemplate.setDocType(1);
		documentTemplate.setTags(Arrays.asList(new Integer[]{1}));
		documentTemplate.setCreateTime(new Date());
		
		boolean result = esService.modifyDocument(documentTemplate);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testdeleteDocument() throws ParseException {
		boolean result = esService.deleteDocument("1");
		Assert.assertTrue(result);
	}
	/***************************************测试搜索******************************************/
	
	//准备数据
	@Test
	public void testPrepareData() throws ParseException {
		ESDocumentTemplate documentTemplate = new ESDocumentTemplate();
		documentTemplate.setDocId("1");
		documentTemplate.setAuthor("张三");
		documentTemplate.setDocName("网上商城需求文档.docx");
		documentTemplate.setDocSummary("网上商城系统是线上销售的主力，为用户提供类似淘宝的一站式购物体验！");
		documentTemplate.setDocType(1);
		documentTemplate.setTags(Arrays.asList(new Integer[]{1,6}));
		documentTemplate.setCreateTime(new Date());
		
		ESDocumentTemplate documentTemplate2 = new ESDocumentTemplate();
		documentTemplate2.setDocId("2");
		documentTemplate2.setAuthor("张三");
		documentTemplate2.setDocName("网上商城开发文档.docx");
		documentTemplate2.setDocSummary("网上商城系统主要技术框架SpringBoot+ElasticSearch，为提升性能，还会引入缓存、集群等多种技术");
		documentTemplate2.setDocType(1);
		documentTemplate2.setTags(Arrays.asList(new Integer[]{1}));
		documentTemplate2.setCreateTime(new Date());
		
		ESDocumentTemplate documentTemplate3 = new ESDocumentTemplate();
		documentTemplate3.setDocId("3");
		documentTemplate3.setAuthor("李四");
		documentTemplate3.setDocName("网上商城项目计划.docx");
		documentTemplate3.setDocSummary("需求分析，方案确认，开发，联调，测试，上线");
		documentTemplate3.setDocType(3);
		documentTemplate3.setTags(Arrays.asList(new Integer[]{1}));
		documentTemplate3.setCreateTime(new Date());
		
		ESDocumentTemplate documentTemplate4 = new ESDocumentTemplate();
		documentTemplate4.setDocId("4");
		documentTemplate4.setAuthor("王五");
		documentTemplate4.setDocName("公司规章总则.docx");
		documentTemplate4.setDocSummary("考勤管理，物资管理，绩效管理");
		documentTemplate4.setDocType(1);
		documentTemplate4.setTags(Arrays.asList(new Integer[]{2,6}));
		documentTemplate4.setCreateTime(new Date());
		
		esService.createDocument(documentTemplate);
		esService.createDocument(documentTemplate2);
		esService.createDocument(documentTemplate3);
		esService.createDocument(documentTemplate4);
	}

	//搜索
	@Test
	public void testBoolQueryFilter() throws ParseException, JsonProcessingException {
		ESSearchReq esSearchReq = new ESSearchReq();
		esSearchReq.setDocType(1);
		esSearchReq.setInputKeyword("文档淘宝");
		//这里前端直接传字符串格式的日期过来即可
		esSearchReq.setBeginTime("2018-7-18 08:12:11");
		esSearchReq.setEndTime("2018-7-19 20:12:11");
		esSearchReq.setTags(Arrays.asList(1));
		ESSearchResp<DocInfo> result = esService.query(esSearchReq);
		System.out.println(mapper.writeValueAsString(result));
	}
	
	//测试聚合查询
	@Test
	public void testAggregationQuery() {
		long total = esService.aggregationQuery("张三");
		System.out.println("作者张三一共有"+total+"篇文章");
	}
}
