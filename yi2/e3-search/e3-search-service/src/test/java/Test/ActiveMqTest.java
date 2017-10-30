package Test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ActiveMqTest {
	@Test
	public void test1() throws IOException{
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
	
		System.in.read();
	}
}
