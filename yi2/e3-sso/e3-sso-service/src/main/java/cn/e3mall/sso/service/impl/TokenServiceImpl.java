package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	private JedisClient redis;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result getUserByToken(String token) {
		String string = redis.get("SESSION:"+token);
		if(StringUtils.isBlank(string)){
			return E3Result.build(201, "用户登录过期");
		}
		redis.expire("SESSION:"+token, SESSION_EXPIRE);
		
		TbUser user = JsonUtils.jsonToPojo(string, TbUser.class);
		return E3Result.ok(user);
	}

}
