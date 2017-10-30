package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private TbOrderMapper tm;
	@Autowired
	private TbOrderItemMapper tim;
	@Autowired
	private TbOrderShippingMapper tsm;
	@Autowired
	private JedisClient redis;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	@Override
	public E3Result createOrder(OrderInfo info) {
		//1.生成订单号
		if(!redis.exists(ORDER_ID_GEN_KEY)){
			redis.set(ORDER_ID_GEN_KEY,ORDER_ID_START);
		}
		System.out.println("redis.1");
		String orderId = redis.incr(ORDER_ID_GEN_KEY).toString();
		//2.补全OrderInfo信息
		info.setOrderId(orderId);
		System.out.println("redis.2");
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		info.setStatus(1);
		info.setCreateTime(new Date());
		info.setUpdateTime(new Date());
		//3.插入订单表
		tm.insert(info);
		System.out.println("redis.3");
		//4.向订单明细表插入数据
		List<TbOrderItem> list = info.getOrderItems();
		System.out.println("redis.4");
		for(TbOrderItem item : list){
			String odId = redis.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全pojo属性
			item.setId(odId);
			item.setOrderId(orderId);
			//插入数据
			tim.insert(item);
		}
		System.out.println("redis.5");
		//5.向订单物流表插入数据
		TbOrderShipping shipping = info.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setCreated(new Date());
		shipping.setUpdated(new Date());
		tsm.insert(shipping);
		return E3Result.ok(orderId);
	}

}
