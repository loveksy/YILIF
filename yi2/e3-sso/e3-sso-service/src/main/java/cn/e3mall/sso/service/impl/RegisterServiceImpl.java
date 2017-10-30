package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.util.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService{
	
	@Autowired
	private TbUserMapper tm;

	@Override
	public E3Result checkData(String parm, int type) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1:用户名  2:手机号  3:邮箱
		if(type==1){
			criteria.andUsernameEqualTo(parm);
		}else if(type==2){
			criteria.andPhoneEqualTo(parm);
		}else if(type==3){
			criteria.andEmailEqualTo(parm);
		}else{
			return E3Result.build(400, "数据类型错误");
		}
		
		List<TbUser> list = tm.selectByExample(example);
		if(list!=null && list.size()>0){
			System.out.println("list!=0:::"+parm+","+type);
			return E3Result.ok(false);
		}
		return E3Result.ok(true);
	}

	@Override
	public E3Result register(TbUser user) {
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())||StringUtils.isBlank(user.getPhone())){
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		
		//1:用户名  2:手机号  3:邮箱
		E3Result checkData1 = checkData(user.getUsername(),1);
		if(!(boolean)checkData1.getData()){
			return E3Result.build(400, "用户名以存在");
		}
		E3Result checkData2 = checkData(user.getPhone(),2);
		if(!(boolean)checkData2.getData()){
			return E3Result.build(400, "手机号以存在");
		}
		//补全pojo的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//password  md5加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		
		tm.insert(user);
		return E3Result.ok();
	}

}
