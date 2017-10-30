package cn.e3mall.search.Message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrServer ss;
	
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage tm = (TextMessage) message;
			String text = tm.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(400);
			//根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			System.out.println("添加成功ksy");
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//向文档对象中添加域
			
			//把文档写入索引库
			ss.add(document);
			//提交
			ss.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
