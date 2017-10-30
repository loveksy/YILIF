package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	JedisClient redis;
	
	@Value("{REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Autowired
	private TbItemMapper tm;
	
	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		// hash  key:用户id  field:商品id  value:商品信息
		
		Boolean flag = redis.hexists(REDIS_CART_PRE+":"+userId, itemId+"");
		//存在，数量相加
		if(flag){
			String json = redis.hget(REDIS_CART_PRE+":"+userId, itemId+"");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			redis.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		TbItem tbItem = tm.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		String image = tbItem.getImage();
		if(StringUtils.isNotBlank(image)){
			tbItem.setImage(image.split(",")[0]);
		}
		redis.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		for(TbItem item : itemList){
			addCart(userId, item.getId(), item.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		List<String> list = redis.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for(String s : list){
			TbItem pojo = JsonUtils.jsonToPojo(s, TbItem.class);
			itemList.add(pojo);
		}
		return itemList;
	}

	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		String string = redis.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
		item.setNum(num);
		
		redis.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		
		redis.hdel(REDIS_CART_PRE+":"+userId, itemId+"");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCartItem(long userId) {
		redis.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}

}
