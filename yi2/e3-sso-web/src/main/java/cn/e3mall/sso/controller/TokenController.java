package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.sso.service.TokenService;

@Controller
public class TokenController {
	
	@Autowired
	private TokenService ts;

	//方式一
	@RequestMapping(value="/user/token/{token}",produces="application/json;charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		
		E3Result result = ts.getUserByToken(token);
		
		//判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			//把结果封装为js语句
			System.out.println("callback"+callback);
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}
	
	//方式二
//	@RequestMapping(value="/user/token/{token}")
//	@ResponseBody
//	public Object getUserByToken(@PathVariable String token,String callback){
//		
//		E3Result result = ts.getUserByToken(token);
//		
//		//判断是否为jsonp请求
//		if(StringUtils.isNotBlank(callback)){
//			//把结果封装为js语句
//			System.out.println("callback"+callback);
//			MappingJacksonValue map = new MappingJacksonValue(result);
//			map.setJsonpFunction(callback);
//			return map;
//		}
//		return result;
//	}
	
}
