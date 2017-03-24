package cn.xulei.spring.es;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.xulei.elastic.domain.Bean2;
import cn.xulei.elastic.domain.Bean3;
import cn.xulei.elastic.domain.TestBean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ElasticsearchServiceTest {

	@Autowired
	private Client client;

	public static ObjectMapper MAPPER = new ObjectMapper();

	@Test
	public void testSearchJsonDoc() {
		long start = System.currentTimeMillis();
		List<TestBean> list = new ArrayList<TestBean>();

		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("ContextLoader");

		QueryBuilder qb1 = QueryBuilders.matchQuery("message", "ContextLoader").operator(Operator.AND);
		SortBuilder sb1 = SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC).unmappedType("date");

		SearchResponse response = client.prepareSearch().addSort(sb1).setTypes().setSearchType(SearchType.DFS_QUERY_THEN_FETCH)// 设置精致查询
				.setQuery(qb1) // 设置查询条件
				// Query
				// .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))
				// // Filter
				.setFrom(0).setSize(60).setExplain(true)// 设置把查询结果展示多少条出来。默认是10条
				.execute().actionGet();
		SearchHits hits = response.getHits();
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("查询的时间差是：" + time);
		System.out.println("查询到的记录数是：" + hits.getTotalHits());
		// 查询到的是一个list集合
		SearchHit[] searchHit = hits.getHits();
		System.out.println(searchHit.length);
		for (SearchHit hit : searchHit) {
			TestBean bean = new TestBean();

			String message = (String) hit.getSource().get("message");
			String host = (String) hit.getSource().get("host");

			bean.setMessage(message);
			bean.setHost(host);
			list.add(bean);

		}
		// System.out.println(list);
	}

	/**
	 * 说明：测试解析后的数据
	 * 
	 * @author 徐磊
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @time：2016年12月28日 下午4:18:54
	 */
	@Test
	public void searchJsonDoc() throws JsonParseException, JsonMappingException, IOException {

		long start = System.currentTimeMillis();
		List<Bean2> list = new ArrayList<Bean2>();

		SortBuilder sb1 = SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC).unmappedType("date");

		QueryBuilder queryBuilder = QueryBuilders.termQuery("tradeNo", "691200001");

		// QueryBuilder queryBuilder2=QueryBuilders.t

		// QueryBuilders.termQuery("tradeNo","201611291447026")
		// <span style="color: red">123</span>
		SearchResponse response = client.prepareSearch().addSort(sb1).setTypes().setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(queryBuilder)
		// Query
				.execute().actionGet();
		SearchHits hits = response.getHits();
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("查询的时间差是：" + time);
		System.out.println("查询到的记录数是：" + hits.getTotalHits());
		// 查询到的是一个list集合
		SearchHit[] searchHit = hits.getHits();
		for (SearchHit hit : searchHit) {

			String json = hit.getSourceAsString();

			// Bean2 bean = MAPPER.readValue(json, Bean2.class);
			Bean2 bean = new Bean2();
			String tradeNo = (String) hit.getSource().get("tradeNo");
			String message = (String) hit.getSource().get("message");
			String host = (String) hit.getSource().get("host");

			bean.setTradeNo(tradeNo);
			bean.setMessage(message);
			bean.setHost(host);
			list.add(bean);
		}
	}

	/**
	 * 多词查询。先查前一个再查后面的一个
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Test
	public void testMatchSearch() throws JsonParseException, JsonMappingException, IOException {

		// QueryBuilder queryBuilder=QueryBuilders.matchQuery("2016-11-27",
		// "FILECFG初始化完成").operator(Operator.AND);

		QueryBuilder queryBuilder = QueryBuilders.matchQuery("message", "2016").operator(Operator.AND);

		long start = System.currentTimeMillis();
		List<Bean2> list = new ArrayList<Bean2>();

		SortBuilder sb1 = SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC).unmappedType("date");

		SearchResponse response = client.prepareSearch().addSort(sb1).setTypes().setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(queryBuilder).addHighlightedField("message")
				.setHighlighterPreTags("<span style=\"color:red\">").setHighlighterPostTags("</span>").setFrom(0).setSize(100) //
				.setExplain(true).execute().actionGet();
		SearchHits hits = response.getHits();
		long end = System.currentTimeMillis();
		System.out.println("查询的时间差是：" + (end - start));
		System.out.println("查询到的记录数是：" + hits.getTotalHits());
		// 查询到的是一个list集合
		SearchHit[] searchHit = hits.getHits();
		for (SearchHit hit : searchHit) {

			String json = hit.getSourceAsString();

			// 获取对应的高亮域
			Map<String, HighlightField> result = hit.highlightFields();
			// 从设定的高亮域中取得指定域
			HighlightField field = result.get("message");
			// 取得定义的高亮标签
			Text[] texts = field.fragments();
			// 为message串值增加自定义的高亮标签
			String message = "";
			for (Text text : texts) {
				message += text;
			}

			// Bean2 bean = MAPPER.readValue(json, Bean2.class);
			Bean2 bean = new Bean2();
			// String tradeNo = (String) hit.getSource().get("tradeNo");
			// 将追加了高亮标签的串值重新填充到对应的对象
			bean.setMessage(message);
			// bean.setHost(host);
			// String message = (String) hit.getSource().get("message");
			String host = (String) hit.getSource().get("host");

			bean.setHost(host);
			list.add(bean);
		}
		try {
			String jsonData = MAPPER.writeValueAsString(list);
			System.out.println(jsonData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取日志文件所在目录.后续和MySSH联合到一起
	 */
	@Test
	public void getFilePaths() {

		QueryBuilder queryBuilder = QueryBuilders.matchQuery("message", "发送数据").operator(Operator.AND);

		long start = System.currentTimeMillis();
		List<Bean3> list = new ArrayList<Bean3>();

		SortBuilder sb1 = SortBuilders.fieldSort("@timestamp").order(SortOrder.ASC).unmappedType("date");

		SearchResponse response = client.prepareSearch()
				.addSort(sb1)
				.setTypes()
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(queryBuilder)
				.setFrom(0).setSize(100) //
				.setExplain(true).execute().actionGet();
		SearchHits hits = response.getHits();
		long end = System.currentTimeMillis();
		System.out.println("查询的时间差是：" + (end - start));
		System.out.println("查询到的记录数是：" + hits.getTotalHits());
		// 查询到的是一个list集合
		SearchHit[] searchHit = hits.getHits();
		for (SearchHit hit : searchHit) {

			String json = hit.getSourceAsString();
			String path = (String)hit.getSource().get("path");
			String message = (String)hit.getSource().get("message");
			
			String host = (String) hit.getSource().get("host");

			Bean3 bean = new Bean3();
			
			bean.setPath(path);
			bean.setHost(host);
			list.add(bean);
		}
		try {
			String jsonData = MAPPER.writeValueAsString(list);
			System.out.println(jsonData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return null;

	}

}
