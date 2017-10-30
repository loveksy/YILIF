package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;

public interface LoginService {
	
	/**
	 * 1.判断用户名密码是否正确
	 * 2.如果正确生成token
	 * 
	 */
	
	E3Result userLogin(String username,String password);
}
