package com.practice.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * ES配置类
 */
@Configuration
public class ESConfig {
	//注入操作es的TransportClient
	@Bean
	public TransportClient client() throws UnknownHostException {
		Settings settings = Settings.builder()
				.put("cluster.name","elasticsearch")//集群名
				.put("client.transport.sniff",true)//自动发现节点
				.build();
		TransportClient client = new PreBuiltTransportClient(settings);
		//如果有多个节点，可以继续调用addXXX()
		client.addTransportAddress(new InetSocketTransportAddress(
				InetAddress.getByName("192.168.128.135"), 9300));
		return client;
	}
	
	//注入对象与json转换的bean，指定日期转换格式
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return objectMapper;
	}
}

