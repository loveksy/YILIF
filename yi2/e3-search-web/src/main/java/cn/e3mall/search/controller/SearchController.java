package cn.e3mall.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {
	
	@Autowired
	SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer rows;
	
	@RequestMapping("/search.html")
	public String searchItemList(String keyword,@RequestParam(defaultValue="1") Integer page,Model model) throws UnsupportedEncodingException{
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		System.out.println(keyword+"??");
		
		SearchResult search = searchService.search(keyword, page, rows);
		
		model.addAttribute("query",keyword);
		model.addAttribute("recourdCount",search.getRecourdCount());
		model.addAttribute("page",page);
		model.addAttribute("totalPages",search.getTotalPages());
		model.addAttribute("itemList",search.getItemList());
		
		//异常测试
		//int a = 1 / 0;
		
		return "search";
	}
}
