package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentCatgoryService;

//内容分类管理

@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCatgoryService contentCatgoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCaatList(@RequestParam(name="id",defaultValue="0")long parentId){
		List<EasyUITreeNode> list = contentCatgoryService.getContentCatList(parentId);
		return list;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result createCategory(Long parentId, String name){
		E3Result result = contentCatgoryService.addContentCategory(parentId, name);
		return result;
	}
	
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateCategory(Long id, String name){
		E3Result result = contentCatgoryService.updateContentCategory(id, name);
		return result;
	}
	
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3Result deleteCategory(Long id){
		E3Result result = contentCatgoryService.deleteContentCategory(id);
		return result;
	}
	
}
