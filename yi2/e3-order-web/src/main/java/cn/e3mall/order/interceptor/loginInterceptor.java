package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class loginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private TokenService ts;
	
	@Autowired
	private CartService cs;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从cookie中取出token，判断是否存在
		String token = CookieUtils.getCookieValue(request, "token");
		if(StringUtils.isBlank(token)){
			//如果不存在，拦截，返回登录页面
			response.sendRedirect("http://localhost:8088/page/login?redirect="+request.getRequestURL());
			return false;
		}
		E3Result result = ts.getUserByToken(token);
		if(result.getStatus()!=200){
			//如果不存在，拦截，返回登录页面
			response.sendRedirect("http://localhost:8088/page/login?redirect="+request.getRequestURL());
			return false;
		}
		TbUser data = (TbUser) result.getData();
		//判断cookie是否有商品，有就合并
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(json)){
			cs.mergeCart(data.getId(), JsonUtils.jsonToList(json, TbItem.class));
		}
		request.setAttribute("user", data);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
