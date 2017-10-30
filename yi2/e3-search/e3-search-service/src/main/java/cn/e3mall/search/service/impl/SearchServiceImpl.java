package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.Dao.SearchDao;
import cn.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SearchDao sd;
	
	@Override
	public SearchResult search(String keyword, int page, int rows)  {
		SolrQuery sq = new SolrQuery();
		sq.setQuery(keyword);
		sq.set("df", "item_title");
		if(page<=0)page=1;
		sq.setStart((page-1)*rows);
		sq.setRows(rows);
		sq.setHighlight(true);
		sq.addHighlightField("item_title");
		sq.setHighlightSimplePre("<em style=\"color:red\">");
		sq.setHighlightSimplePost("</em>");
		
	
		int totalItem;
		try {
			SearchResult search = sd.search(sq);
			totalItem = (int) search.getRecourdCount();
			int totalpage = totalItem / rows;
			if(totalItem % rows !=0)
				totalpage++ ;
			search.setTotalPages(totalpage);
			return search;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
