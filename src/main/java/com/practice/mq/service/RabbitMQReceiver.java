package com.practice.mq.service;

import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.practice.bus.bean.DocInfo;
import com.practice.bus.bean.EnumOperation;
import com.practice.config.RabbitMQConfig;
import com.practice.es.service.ESDocumentTemplate;
import com.practice.es.service.ESService;

//从指定队列中接收消息
@RabbitListener(queues=RabbitMQConfig.QUEUE_NAME)
public class RabbitMQReceiver {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ESService indexService;
	
	//收到消息后，将调用该方法处理
	@RabbitHandler
	public void handleMessage(RabbitMessage rabbitMessage) throws InterruptedException {
		logger.debug("消费者"+this+"收到MQ消息:"+rabbitMessage);
		
		EnumOperation operation = rabbitMessage.getOperation();
		DocInfo docInfo = rabbitMessage.getDocInfo();
		switch(operation) {
		case ADD:
			//TODO 使用POI读取文档，生成自动摘要 模拟处理需要3s
			Thread.sleep(3000);

			//TODO 构造索引文档（这里做一个模拟，实际需要根据docInfo来构造）
			ESDocumentTemplate documentTemplate = new ESDocumentTemplate();
			documentTemplate.setDocId(docInfo.getDocId()+"");
			documentTemplate.setAuthor("张三");
			documentTemplate.setDocName("网上商城需求文档.docx");
			documentTemplate.setDocSummary("网上商城系统主要技术框架SpringBoot+ElasticSearch，为用户提供类似淘宝的一站式购物体验！");
			documentTemplate.setDocType(1);
			documentTemplate.setTags(Arrays.asList(new Integer[]{1,6}));
			documentTemplate.setCreateTime(new Date());
			//让ES索引该文档信息
			indexService.createDocument(documentTemplate);
			break;
		case MODIFY:
			//TODO 构造索引文档（这里做一个模拟，实际需要根据docInfo来构造）
			ESDocumentTemplate documentTemplateNew = new ESDocumentTemplate();
			documentTemplateNew.setDocId(docInfo.getDocId()+"");
			documentTemplateNew.setAuthor("张三");
			documentTemplateNew.setDocName("网上商城需求文档.docx");
			documentTemplateNew.setDocSummary("网上商城系统主要技术框架SpringBoot+ElasticSearch，为用户提供类似淘宝的一站式购物体验，欢迎来使用！");
			documentTemplateNew.setDocType(1);
			documentTemplateNew.setTags(Arrays.asList(new Integer[]{1}));
			//让ES索引该文档信息
			indexService.modifyDocument(documentTemplateNew);
			break;
		case DELETE:
			//让ES删除该文档的索引
			indexService.deleteDocument(docInfo.getDocId()+"");
		}
	}
	
	
}
