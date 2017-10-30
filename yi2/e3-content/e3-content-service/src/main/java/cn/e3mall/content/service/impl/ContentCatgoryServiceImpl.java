package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentCatgoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCatgoryServiceImpl implements ContentCatgoryService{

	@Autowired
	private TbContentCategoryMapper cotentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria c = example.createCriteria();
		c.andParentIdEqualTo(parentId);
		List<TbContentCategory> list1 = cotentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> list2 = new ArrayList<>();
		for(TbContentCategory o : list1){
			EasyUITreeNode e = new EasyUITreeNode();
			e.setId(o.getId());
			e.setState(o.getIsParent()?"closed":"open");
			e.setText(o.getName());
			list2.add(e);
		}
		return list2;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 1、接收两个参数：parentId、name
		// 2、向tb_content_category表中插入数据。
		// a)创建一个TbContentCategory对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		// b)补全TbContentCategory对象的属性
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		tbContentCategory.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		tbContentCategory.setStatus(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		// c)向tb_content_category表中插入数据
		cotentCategoryMapper.insert(tbContentCategory);
		
		// 3、判断父节点的isparent是否为true，不是true需要改为true。
				TbContentCategory parentNode = cotentCategoryMapper.selectByPrimaryKey(parentId);
				if (!parentNode.getIsParent()) {
					parentNode.setIsParent(true);
					//更新父节点
					cotentCategoryMapper.updateByPrimaryKey(parentNode);
				}

		// 4、需要主键返回。
		// 5、返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(tbContentCategory);
	}

	@Override
	public E3Result updateContentCategory(long id, String name) {
		//cotentCategoryMapper.updateBy
		TbContentCategory tc = cotentCategoryMapper.selectByPrimaryKey(id);
		tc.setName(name);
		cotentCategoryMapper.updateByPrimaryKey(tc);
		
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContentCategory(long id) {
		TbContentCategory tc = cotentCategoryMapper.selectByPrimaryKey(id);
		long parentId = tc.getParentId();
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria c = example.createCriteria();
		c.andParentIdEqualTo(tc.getParentId());
		List<TbContentCategory> list= cotentCategoryMapper.selectByExample(example);
		if(tc.getIsParent()){
			return E3Result.build(201, "不能刪除目录");
		}
		if(list.size()==1){
			TbContentCategory parent = cotentCategoryMapper.selectByPrimaryKey(parentId);
			parent.setIsParent(false);
		}
		cotentCategoryMapper.deleteByPrimaryKey(id);
		
		
		
		return E3Result.ok();
	}

}
