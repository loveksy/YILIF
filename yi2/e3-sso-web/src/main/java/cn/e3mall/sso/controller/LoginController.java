package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.sso.service.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	@RequestMapping("/page/login")
	public String showLogin(String redirect,Model model	){
		model.addAttribute("redirect",redirect);
		return "login";
	}
	
//	@RequestMapping("/user/login")
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		E3Result login = loginService.userLogin(username, password);
		if(login.getStatus()==200){
			String token = login.getData().toString();
			//将token写入cookie
			CookieUtils.setCookie(request, response, "token", token);
		}
		
		return login;
	}
}
