package com.practice.es.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.bus.bean.DocInfo;

/**
 * 需要提前使用REST AIP创建索引 映射 
 * ①发送put 请求到http://192.168.128.135:9200/doc 其中doc为索引名称
 * ②参数为json格式的数据 mapping.json
 * 
 * 创建/修改/删除ES Document的类
 *
 */
@Service
public class ESService {
	@Autowired
	TransportClient transportClient;
	@Autowired
	ObjectMapper objectMapper;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private final static String INDEX = "doc";
	private final static String TYPE = "office";
	
	
	
	/**添加文档
	 * @throws JsonProcessingException **/
	public boolean createDocument(ESDocumentTemplate documentTemplate){
		try {
			//将对象转换成json
			String json = objectMapper.writeValueAsString(documentTemplate);
			logger.debug("ES创建文档:"+json);
			
			IndexResponse response = transportClient.prepareIndex(INDEX, TYPE, documentTemplate.getDocId()).setSource(json, XContentType.JSON).get();
			if(response.status() == RestStatus.CREATED) {
				return true;
			}
		}catch(Exception e) {
			logger.error("ES添加文档失败",e);
		}
		return false;
	}
	
	/**修改文档**/
	public boolean modifyDocument(ESDocumentTemplate documentTemplate) {
		try {
			//将对象转换成json
			String json = objectMapper.writeValueAsString(documentTemplate);
			logger.debug("ES修改文档:"+json);
			
			UpdateResponse response = transportClient.prepareUpdate(INDEX, TYPE, documentTemplate.getDocId()).setDoc(json, XContentType.JSON).get();
			if(response.status() == RestStatus.OK) {
				return true;
			}	
		}catch(Exception e) {
			logger.error("ES修改文档失败",e);
		}
		return false;
	}
	
	/**删除文档**/
	public boolean deleteDocument(String docId) {
		try {
			//将对象转换成json
			logger.debug("ES删除文档:"+docId);
			
			DeleteResponse response = transportClient.prepareDelete(INDEX, TYPE, docId).get();
			if(response.status() == RestStatus.OK) {
				return true;
			}	
		}catch(Exception e) {
			logger.error("ES修改文档失败",e);
		}
		return false;
	}
	
	/**根据用户输入条件进行查询
	 * @throws ParseException **/
	public ESSearchResp<DocInfo> query(ESSearchReq esSearchReq) throws ParseException {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		//针对keyword进行过滤
		if(!StringUtils.isEmpty(esSearchReq.getDocType())) {
			boolQuery.filter(QueryBuilders.termQuery(ESFieldName.DOC_TYPE, esSearchReq.getDocType()));	
		}
		if(!StringUtils.isEmpty(esSearchReq.getAuthor())) {
			boolQuery.filter(QueryBuilders.termQuery(ESFieldName.AUTHOR, esSearchReq.getAuthor()));	
		}
		
		//针对text进行全文检索 查询docName OR docSummary满足匹配要求的结果
		if(!StringUtils.isEmpty(esSearchReq.getInputKeyword())) {
			boolQuery.must(QueryBuilders.multiMatchQuery(esSearchReq.getInputKeyword(), ESFieldName.DOC_NAME,ESFieldName.DOC_SUMMARY));
		}
		
		//针对时间进行范围查询
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(ESFieldName.CREATE_TIME);
		if(esSearchReq.getBeginTime() != null) {
			boolQuery.filter(rangeQuery.format("yyyy-MM-dd HH:mm:ss").gte(esSearchReq.getBeginTime()));
		}
		if(esSearchReq.getEndTime() != null) {
			boolQuery.filter(rangeQuery.format("yyyy-MM-dd HH:mm:ss").lte(esSearchReq.getEndTime()));
		}
		
		//针对数组查询
		if(esSearchReq.getTags().size() > 0) {
			boolQuery.filter(QueryBuilders.termsQuery(ESFieldName.TAGS, esSearchReq.getTags()));
		}
		
		//高亮显示
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<strong>");
		highlightBuilder.postTags("</strong>");
		highlightBuilder.field(ESFieldName.DOC_NAME).field(ESFieldName.DOC_SUMMARY);
		
		//排序 分页
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(INDEX).setTypes(TYPE).setQuery(boolQuery)
		//.addSort(ESFieldName.CREATE_TIME,SortOrder.DESC)//默认是按照相关性的得分来排序的
		.setFrom(esSearchReq.getFrom())
		.setSize(esSearchReq.getSize())
		.highlighter(highlightBuilder);//对匹配内容高亮显示
		//.setFetchSource(ESFieldName.DOC_ID, null);//可以设置es查询返回指定字段，默认为全部返回
		
		logger.debug(searchRequestBuilder.toString());
		
		SearchResponse searchResponse = searchRequestBuilder.get();
		List<DocInfo> result = new ArrayList<>();
		
		if(searchResponse.status() != RestStatus.OK) {
			logger.error("测试boolean查询失败："+searchRequestBuilder);
			return new ESSearchResp<DocInfo>(0, result);
		}else {
			for (SearchHit hit : searchResponse.getHits()) {
				DocInfo docInfo = new DocInfo();
				docInfo.setDocId(Long.parseLong((String)hit.getSource().get(ESFieldName.DOC_ID)));
				Map<String, HighlightField> highlightFields = hit.getHighlightFields();
				//如果有高亮信息，用高亮信息 这样前端取出相应值就能直接在html中展示了
				if(highlightFields.get(ESFieldName.DOC_NAME)!=null) {
					docInfo.setDocName(highlightFields.get(ESFieldName.DOC_NAME).getFragments()[0].string());
				}else {
					docInfo.setDocName((String)hit.getSource().get(ESFieldName.DOC_NAME));	
				}
				if(highlightFields.get(ESFieldName.DOC_SUMMARY)!=null) {
					docInfo.setDocSummary(highlightFields.get(ESFieldName.DOC_SUMMARY).getFragments()[0].string());
				}else {
					docInfo.setDocSummary((String)hit.getSource().get(ESFieldName.DOC_SUMMARY));	
				}
				docInfo.setAuthor((String)hit.getSource().get(ESFieldName.AUTHOR));
				docInfo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String)hit.getSource().get(ESFieldName.CREATE_TIME)));
				result.add(docInfo);
			}
			return new ESSearchResp<DocInfo>(searchResponse.getHits().getTotalHits(), result);
		}
	}
	
	/**
	 * 聚合查询
	 */
	public long aggregationQuery(String author) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		if(!StringUtils.isEmpty(author)) {
			boolQuery.filter(QueryBuilders.termQuery(ESFieldName.AUTHOR, author));	
		}
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(INDEX).setTypes(TYPE)
				.setQuery(boolQuery)
				.addAggregation(AggregationBuilders.terms(ESFieldName.AGGREGATION_OF_AUTHOR)//聚合结果对应的名称
						.field(ESFieldName.AUTHOR))//对那个字段执行聚合 类似group by后面的字段
				.setSize(0);//表示只要聚合结果，不要其他具体的数据（作者 文档名 时间等等）
		
		logger.debug(searchRequestBuilder.toString());
		
		SearchResponse searchResponse = searchRequestBuilder.get();
		
		if(searchResponse.status() != RestStatus.OK) {
			logger.error("测试聚合查询失败："+searchRequestBuilder);
		}else {
			Terms terms = searchResponse.getAggregations().get(ESFieldName.AGGREGATION_OF_AUTHOR);
            if (terms.getBuckets() != null && !terms.getBuckets().isEmpty()) {
                //这里查出指定作者一共有多少篇文章
            	return terms.getBucketByKey(author).getDocCount();
            }
		}
		return 0L;
	}
}
