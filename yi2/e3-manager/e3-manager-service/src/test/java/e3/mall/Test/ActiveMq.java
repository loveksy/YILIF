package e3.mall.Test;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ActiveMq {
	
	//点到点
	@Test
	public void test1() throws JMSException{
		//1.创建一个连接工厂对象，需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//2.使用工厂对象创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，Connection对象的start方法
		connection.start();
		//4.创建一个Session对象
		//第一个参数：是否开启事务(一般不开启事务，若开启，第二个参数无意义)  
		//第二个参数：应答模式  自动应答或手动应答   一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象。两种形式queue，topic，现在是queue
		Queue queue = session.createQueue("queue1");
		//6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个Message对象，可以使用TextMessage
		//TextMessage textMessage = new ActiveMQTextMessage();
		//textMessage.setText("hello");
		TextMessage message = session.createTextMessage("loveksy!");
		//8.发送消息
		producer.send(message);
		
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}


	@Test
	public void test2() throws JMSException, IOException{
		//conn
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		Connection connection = factory.createConnection();
		connection.start();
		//session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("spring-queue");
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				TextMessage m1 = (TextMessage)message;
				try {
					String text = m1.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	//topic
	@Test
	public void test3() throws JMSException{
		//1.创建一个连接工厂对象，需要指定服务的ip及端口
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
				//2.使用工厂对象创建一个Connection对象
				Connection connection = connectionFactory.createConnection();
				//3.开启连接，Connection对象的start方法
				connection.start();
				//4.创建一个Session对象
				//第一个参数：是否开启事务(一般不开启事务，若开启，第二个参数无意义)  
				//第二个参数：应答模式  自动应答或手动应答   一般为自动应答
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				//5.使用Session对象创建一个Destination对象。两种形式queue，topic，现在是queue
//				Queue queue = session.createQueue("queue1");
				Topic topic = session.createTopic("topic1");
				//6.使用Session对象创建一个Producer对象
				MessageProducer producer = session.createProducer(topic);
				//7.创建一个Message对象，可以使用TextMessage
				//TextMessage textMessage = new ActiveMQTextMessage();
				//textMessage.setText("hello");
				TextMessage message = session.createTextMessage("topic:loveksy!");
				//8.发送消息
				producer.send(message);
				
				//9.关闭资源
				producer.close();
				session.close();
				connection.close();
	}
	
	
	@Test
	public void test4() throws JMSException, IOException{ 
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		Connection connection = factory.createConnection();
		connection.start();
		//session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		Queue queue = session.createQueue("queue1");
		Topic topic = session.createTopic("topic1");
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				TextMessage m1 = (TextMessage)message;
				try {
					String text = m1.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic3...start");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
