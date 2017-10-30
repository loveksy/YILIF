package Test;

import java.io.IOException;
import java.util.Collection;

import javax.management.Query;
import javax.swing.text.Document;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	@Test
	public void test1() throws SolrServerException, IOException{
		//1.创建一个集群的连接，CloudSolrServer对象
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.128:2182,192.168.25.128:2183,192.168.25.128:2184");
		
		//2.zkHost:zookeeper的地址列表
		//2.设置一个defaultCollection属性
		solrServer.setDefaultCollection("collection1");
		//3.创建一个文档对象，并添加域
		SolrInputDocument sid = new SolrInputDocument();
		sid.setField("id", "lining");
		sid.setField("item_title", "lovesuyan");	
		//4.把文档写入索引库
		solrServer.add(sid);
		//5.提交
		solrServer.commit();
	}
	
	
	@Test
	public void test2() throws SolrServerException{
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.128:2182,192.168.25.128:2183,192.168.25.128:2184");
		solrServer.setDefaultCollection("collection1");
		
		SolrQuery sq = new SolrQuery();
		sq.setQuery("*:*");
		QueryResponse query = solrServer.query(sq);
		SolrDocumentList results = query.getResults();
		System.out.println(results.getNumFound());
		for(SolrDocument sd : results){
			System.out.println(sd.get("id"));
			System.out.println(sd.get("item_title"));
		}
	}
}
