package jedis_test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

public class test2 {
	
	@Test
	public void test1(){ 
		ApplicationContext application = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient c = application.getBean(JedisClient.class);
		System.out.println(1);
		System.out.println(c.get("weiyi"));
		
	}
}
