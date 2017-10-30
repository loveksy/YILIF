package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SerarchItemService;

@Service
public class SerarchItemServiceImpl implements SerarchItemService{

	@Autowired  //Autowired是根据类型注入的
	ItemMapper im;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public E3Result importAllItems() {
		//String url = "http://192.168.25.128:8080/solr";
		//SolrServer ss = new HttpSolrServer(url);
		List<SearchItem> itemList = im.getItemList();
		
		try{
			for(SearchItem searchItem : itemList){
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());

				
				solrServer.add(document);
			}
			solrServer.commit();
		}catch(Exception e){
			e.printStackTrace();
			return E3Result.build(500, "导入失败");
		}
		return E3Result.ok();
	}

}
