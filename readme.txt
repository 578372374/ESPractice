简介：
	使用SpringBoot+ES+RabbitMQ实现文档搜索功能，包含常用操作，没有业务代码干扰，拿来即可用
主要包含的内容:
	1.ES创建Index Mapping的全过程（包含日期 数组映射，中文分词等）
	2.ES对Document的增删改查与业务逻辑的结合
	3.ES常用查询（包含boolQuery filter termQuery rangeQuery multiMatchQuery，
		聚合，分页排序，关键字高亮[可直接在html中展示]，数组查询，时间范围查询等）
	4.SpringBoot整合RabbitMQ，实现ES对Document的操作与业务逻辑的解耦
业务介绍：
	假设我们有一个文档管理系统，用户可以上传文档/修改文档/删除文档/对文档添加标签，现在我们需要对
		系统的文档进行搜索，可以按照类型（ppt word excel）/作者搜索，按照文件名/摘要进行全文匹配，
		按照创建时间进行范围查询，按照文档标签（一个文档可以有多个标签）进行搜索。
	在用户添加文档后，我们需要对文档进行分析，获得文档摘要，用ES进行索引，所以我们引入了RabbitMQ
		来实现这一步。
	注意我们的系统是文档管理系统，这里的文档和ES的Document是完全不同的两个概念，请注意区分！
代码结构：
	bus包，即为业务逻辑相关代码
	config包，ES和RabbitMQ相关配置类
	es.service包，ES相关代码
	mq.service包，MQ相关代码
	ESJson文件夹，ES Mapping的json数据
	
	ElasticsearchApplicationTests.java ES的相关测试，包含创建数据/查询
	DocController.java MQ的相关测试
使用说明：
	1.参看json文件的说明.txt，创建ES Index和Mapping
	2.执行ES测试，创建数据，测试查询
	3.执行MQ测试，测试MQ的功能
	
