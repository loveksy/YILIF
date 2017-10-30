package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

//监听商品添加消息，生成对应的静态页面
public class HtmlGenListener implements MessageListener{

	@Autowired
	private ItemService itemService;
	
	@Autowired
	FreeMarkerConfigurer free;
	
	@Value("${HTML_GEN_PATH}")
	private String PATH;
	@Override
	public void onMessage(Message message) {
		//1.从消息中取商品id
		TextMessage tm = (TextMessage) message;
		try {
			String text = tm.getText();
			Long itemId = new Long(text);
			System.out.println(itemId);
			//等待事务提交
			Thread.sleep(500);
			
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			
			Configuration configuration = free.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			System.out.println(PATH);
			Writer out = new FileWriter(new File(PATH+itemId+".html"));
			
			template.process(data, out); 
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//2.创建一个模板
		//3.根据id查询商品信息
		//4.创建数据集，封装数据
	}

}
