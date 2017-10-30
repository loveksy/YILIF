package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatlist(long parentId) {
		// 根据parentId查询子节点列表     
		TbItemCatExample example = new TbItemCatExample();
		Criteria c = example.createCriteria();
		c.andParentIdEqualTo(parentId);
		//2.执行查询得到list1
		List<TbItemCat> list1 = itemCatMapper.selectByExample(example);
		//3.创建返回的list2
		List<EasyUITreeNode> list2 = new ArrayList<>();
		//4.通过list1给list2赋值
		for(TbItemCat tc : list1){
			EasyUITreeNode en = new EasyUITreeNode();
			en.setId(tc.getId());
			en.setText(tc.getName());
			en.setState(tc.getIsParent()?"closed":"open");
			list2.add(en);
		}
		
		//5.返回list2
		return list2;
	}
	
	
}
