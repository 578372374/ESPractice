package com.practice.bus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.bus.bean.DocInfo;
import com.practice.bus.bean.EnumOperation;
import com.practice.config.RabbitMQConfig;
import com.practice.es.service.ESService;
import com.practice.mq.service.RabbitMessage;

/**
 * 文档业务CRUD对应的Service
 *
 */
@Service
public class DocService {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ESService esService;
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	/**
	 * @param docInfo
	 */
	public void createDoc(DocInfo docInfo) {
		//TODO 这里拿到文档信息，调用dao执行入库
		
		//入库成功后，向RabbitMQ发消息 由消费者来解析文件，提取摘要，构造documentTemplate，索引文档
		RabbitMessage rabbitMessage = new RabbitMessage();
		rabbitMessage.setOperation(EnumOperation.ADD);
		rabbitMessage.setDocInfo(docInfo);
		rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,rabbitMessage);
		logger.debug("创建文档，发送MQ消息："+rabbitMessage);
	}
	
	/**
	 * @param docInfo
	 */
	public void modifyDoc(DocInfo docInfo) {
		//TODO 这里拿到文档信息，调用dao执行更新
		
		//更新成功后，向RabbitMQ发消息 由消费者来解析文件，提取摘要，构造documentTemplate，索引文档
		RabbitMessage rabbitMessage = new RabbitMessage();
		rabbitMessage.setOperation(EnumOperation.MODIFY);
		rabbitMessage.setDocInfo(docInfo);
		rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,rabbitMessage);
		logger.debug("修改文档，发送MQ消息："+rabbitMessage);
	}
	
	public void deleteDoc(DocInfo docInfo) {
		//TODO 这里拿到文档信息，调用dao执行删除
		
		//删除成功后，向RabbitMQ发消息 由消费者来通知es删除被索引文档
		RabbitMessage rabbitMessage = new RabbitMessage();
		rabbitMessage.setOperation(EnumOperation.DELETE);
		rabbitMessage.setDocInfo(docInfo);
		rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,rabbitMessage);
		logger.debug("删除文档，发送MQ消息："+rabbitMessage);
	}
}
