package cn.e3mall.search.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer ss;
	
	public SearchResult search(SolrQuery query) throws SolrServerException{
		//查询得到结果QueryResponse
		QueryResponse res = ss.query(query);
		//得到总记录数
		SolrDocumentList results = res.getResults();
		long total = results.getNumFound();
		SearchResult sr = new SearchResult();
		sr.setRecourdCount(total);
		List<SearchItem> itemlist = new ArrayList<>();
		Map<String, Map<String, List<String>>> highlighting = res.getHighlighting();
		//得到list
		for(SolrDocument sd : results){
			SearchItem item = new SearchItem();
			item.setId((String)sd.get("id"));
			item.setCategory_name((String)sd.get("item_category_name"));
			item.setImage((String)sd.get("item_image"));
			item.setPrice((long) sd.get("item_price"));
			item.setSell_point((String) sd.get("item_sell_point"));
			//item.setTitle((String) sd.get("item_title"));
			List<String> list = highlighting.get(sd.get("id")).get("item_title");
			if(list!=null && list.size()>0){
				item.setTitle(list.get(0));
			}else{
				item.setTitle((String) sd.get("item_title"));
			}
			itemlist.add(item);
		}
		sr.setItemList(itemlist);
		return sr;
	}
}
