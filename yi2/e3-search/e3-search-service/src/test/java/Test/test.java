package Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class test {
	
	@Test
	public void test1() throws SolrServerException, IOException{
		//SolrServer
		String url = "http://192.168.25.128:8080/solr/";
		SolrServer ss = new HttpSolrServer(url);
		//Document
		SolrInputDocument sid = new SolrInputDocument();
		//设置document的域
		sid.setField("id", "id01"); 
		sid.addField("item_title", "测试商品");
		sid.addField("item_price", "199");

		//将document加入索引库
		ss.add(sid);
		//提交
		ss.commit();
	}
	
	@Test
	public void test2() throws SolrServerException, IOException{ 
		//SolrServer
		String url = "http://192.168.25.128:8080/solr/";
		SolrServer ss = new HttpSolrServer(url);
		//delete  id  query
		ss.deleteByQuery("id:id01");
		//commit
		ss.commit();
	}
	
	@Test
	public void test3() throws SolrServerException{
		//1.SolrServer对象
		String url = "http://192.168.25.128:521/solr/";
		SolrServer ss = new HttpSolrServer(url);
		//2.Query对象
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		//3.查询得到结果对象
		QueryResponse result = ss.query(query);
		//4.从结果中获取文档
		SolrDocumentList results = result.getResults();
		System.out.println("all:"+results.getNumFound());
		//5.遍历文档，输出
		for(SolrDocument sd : results){
			System.out.println(sd.get("id"));
			System.out.println(sd.get("item_title"));
			System.out.println(sd.get("item_sell_point"));
			System.out.println(sd.get("item_price"));
			System.out.println(sd.get("item_category_name"));
			System.out.println(sd.get("_version_"));
		}
	}
	
	@Test
	public void test4() throws SolrServerException{
		//1.SolrServer
		String url = "http://192.168.25.128:521/solr";
		SolrServer ss = new HttpSolrServer(url);
		//2.SolrQuery
		SolrQuery query = new SolrQuery();
		query.setQuery("手机");
		query.set("df", "item_title");
		query.setStart(1);
		query.setRows(5);
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		//3.QueryResponse
		QueryResponse res = ss.query(query);
		//4.SolrDocumentList
		SolrDocumentList list = res.getResults();
		//5.遍历List
		for(SolrDocument sd : list){
			System.out.println(sd.get("id"));
			//高亮
			Map<String, Map<String, List<String>>> highlighting = res.getHighlighting();
			Map<String, List<String>> map = highlighting.get(sd.get("id"));
			List<String> list2 = map.get("item_title");
			if(list2!=null && list2.size()>0){
				System.out.println(list2.get(0));
//				for(String str : list2){
//					System.out.println(str);
//				}
			}else{
				System.out.println(sd.get("item_title"));
			}
			
			System.out.println(sd.get("item_sell_point"));
			System.out.println(sd.get("item_price"));
			System.out.println(sd.get("item_category_name"));
			System.out.println(sd.get("_version_"));
		}
	}
	
}
