package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService rs;
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		E3Result data = rs.checkData(param, type);
		return data;
	}
	
	
	@RequestMapping("/user1/check1/{param}/{type}")
	@ResponseBody
	public user checkData1(@PathVariable String param,@PathVariable Integer type){
		user u = new user(param, type, "上海");
		return u;
	}
	
	
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result result = rs.register(user);
		return result;
	}
	

}
