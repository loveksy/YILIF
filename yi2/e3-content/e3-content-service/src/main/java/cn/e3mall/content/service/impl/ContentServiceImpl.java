package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService{
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	

	@Override
	public E3Result addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		tbContentMapper.insert(content);
		//缓存同步,删除缓存中的数据
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId()+"");
		
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查询缓存
//		try{
//			//如果缓存中直接响应结果
//			String json = jedisClient.hget(CONTENT_LIST, cid+"");
//			if(StringUtils.isNotBlank(json)){
//				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
//				System.out.println("缓存...");
//				return list;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		//如果没有查询数据库
		TbContentExample example = new TbContentExample();
		Criteria c = example.createCriteria();
		c.andCategoryIdEqualTo(cid);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		//把结果添加到缓存
//		try{
//			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return list;
	}

	@Override
	public EasyUIDataGridResult getContentListFYByCid(Integer page, Integer rows, long cid) {
				// 设置分页信息
				PageHelper.startPage(page, rows);
				//执行查询
				TbContentExample example = new TbContentExample();
				Criteria c = example.createCriteria();
				c.andCategoryIdEqualTo(cid);
				List<TbContent> list = tbContentMapper.selectByExample(example);
				EasyUIDataGridResult result = new EasyUIDataGridResult();
				result.setRows(list);
				//取分页结果
				PageInfo<TbContent> info = new PageInfo<>(list); 
				long total = info.getTotal();
				result.setTotal(total);
		return result;
	}


}
