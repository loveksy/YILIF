package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbUser;

public interface TokenService {
	E3Result getUserByToken(String token);
}
