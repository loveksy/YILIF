package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {

	
	@Autowired
	private CartService cs;
	@Autowired
	private OrderService os;
	
	@RequestMapping("order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		System.out.println("order:1");
		if(request.getAttribute("user")==null){
			System.out.println("order user is null");
		}else{
			System.out.println("order user is not null");
		}
		TbUser user = (TbUser)request.getAttribute("user");
		
		List<TbItem> list = cs.getCartList(user.getId());
		
		request.setAttribute("cartList", list);
		return "order-cart";
	}
	
	
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//1.取用户信息,并添加到orderInfo中
		System.out.println("createorder1");
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//2.调用服务生成订单
		System.out.println("createorder2");
		E3Result e3Result = os.createOrder(orderInfo);
		System.out.println("createorder3");
		if(e3Result.getStatus()==200){
			//清空购物车
			System.out.println("createorder4");
			cs.clearCartItem(user.getId());
		}
		//3.
		System.out.println("createorder5");
		request.setAttribute("orderId", e3Result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		System.out.println("order success but!");
		return "success";
	}
}
