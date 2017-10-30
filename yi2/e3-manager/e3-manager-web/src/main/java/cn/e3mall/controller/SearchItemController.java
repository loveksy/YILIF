package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.search.service.SerarchItemService;

//功能：导入商品到索引库

@Controller
public class SearchItemController {

	@Autowired
	private SerarchItemService service;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemList(){
		E3Result importAllItems = service.importAllItems();
		return importAllItems;
	}
}
