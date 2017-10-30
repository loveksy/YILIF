package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	
	@Autowired
	private ItemService is;
	
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	@Autowired
	private CartService cs;
	
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1")Integer num,HttpServletRequest request,HttpServletResponse response){
		
		//判断是否已经登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			//若登录，redis
			System.out.println("redis 添加成功了！！！");
			cs.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		
		//若未登录，cookie
		
		List<TbItem> list = getCartListFromCookie(request);
		boolean flag = false;
		for(TbItem tbItem : list){
			if(tbItem.getId() == itemId.longValue()){
				//找到商品，数量相加
				tbItem.setNum(tbItem.getNum()+num);
				flag = true;
				//跳出循环
				break;
			}
		}
		if(!flag){
			TbItem tbItem = is.getItemById(itemId); 
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)){
				String string = image.split(",")[0];
				tbItem.setImage(string);
			}
			list.add(tbItem);
		}
		//写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list),COOKIE_CART_EXPIRE,true);
		return "cartSuccess";
	}
	
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		
		String json = CookieUtils.getCookieValue(request, "cart",true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	
	@RequestMapping("/cart/cart")
	public String ShowCartList(HttpServletRequest request,HttpServletResponse response){
		
		List<TbItem> list = getCartListFromCookie(request);
		
		//判断是否已经登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			System.out.println("user show!!!");
			if(list!=null){
				cs.mergeCart(user.getId(), list);
			}
			CookieUtils.deleteCookie(request, response, "cart");
			
			List<TbItem> cartList = cs.getCartList(user.getId());
			
			request.setAttribute("cartList", cartList);
			return "cart";
		}
		
		
		request.setAttribute("cartList", list);
		return "cart";
	}
	
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
		//判断是否已经登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			System.out.println("user update!");
			cs.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		
		
		List<TbItem> list = getCartListFromCookie(request);
		
		for(TbItem item : list){
			if(item.getId()==itemId.longValue()){
				item.setNum(num);
				break;
			}
		}
		
		//写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list),COOKIE_CART_EXPIRE,true);
		return E3Result.ok();
	}
	
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		
		//判断是否已经登录
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			System.out.println("user delete!");
			cs.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		
		List<TbItem> list = getCartListFromCookie(request);
		
		for(TbItem item : list){
			if(item.getId()==itemId.longValue()){
				list.remove(item);
				break;
			}
		}
		
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list),COOKIE_CART_EXPIRE,true);
		
		return "redirect:/cart/cart.html";
	}
}
