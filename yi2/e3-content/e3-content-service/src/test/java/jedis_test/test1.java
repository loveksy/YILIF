package jedis_test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class test1 {
	
	@Test
	public void test1(){
		//创建一个连接Jedis对象，参数host，port
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		//使用jedis操作redis，所有redis命令对应一个方法
		jedis.set("love", "ksy");
		String string = jedis.get("love");
		System.out.println(string);
		//关闭连接
		jedis.close();
	}
	
	@Test
	public void test2(){ 
		JedisPool pool = new JedisPool("192.168.25.128", 6379);
		Jedis jedis = pool.getResource();
		String s = jedis.get("love");
		System.out.println("love:"+s);
		jedis.close();
		pool.close();
	}
	
	@Test 
	public void test3(){
		 Set<HostAndPort> nodes = new HashSet<>();
		 nodes.add(new HostAndPort("192.168.25.128", 7001));
		 nodes.add(new HostAndPort("192.168.25.128", 7002));
		 nodes.add(new HostAndPort("192.168.25.128", 7003));
		 nodes.add(new HostAndPort("192.168.25.128", 7004));
		 nodes.add(new HostAndPort("192.168.25.128", 7005));
		 nodes.add(new HostAndPort("192.168.25.128", 7006));
		 JedisCluster jc = new JedisCluster(nodes);
		 jc.set("weiyi", "sy");
		 System.out.println("my weiyi:"+jc.get("weiyi"));
		 
	}
}
