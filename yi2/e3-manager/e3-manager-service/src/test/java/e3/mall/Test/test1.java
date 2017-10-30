package e3.mall.Test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test1 {
	
	
	@Test
	public void test1() throws InterruptedException{ 
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		while(true){
			Thread.sleep(1000);
		}
	
	}
}
