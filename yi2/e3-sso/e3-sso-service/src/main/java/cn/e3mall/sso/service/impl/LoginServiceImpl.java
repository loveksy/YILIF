package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private TbUserMapper tm;
	
	@Autowired
	private JedisClient redis;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result userLogin(String username, String password) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tm.selectByExample(example);
		if(list == null || list.size()==0){
			return E3Result.build(4,"username is error");
		}
		
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			return E3Result.build(4,"password is error");
		}
		//用户正确，生成token
		String token = UUID.randomUUID().toString();
		//存入redis
		user.setPassword(null);
		redis.set("SESSION:"+token, JsonUtils.objectToJson(user));
		System.out.println("过期时间："+SESSION_EXPIRE); 
		redis.expire("SESSION:"+token,SESSION_EXPIRE );
		return E3Result.ok(token);
	}

}
