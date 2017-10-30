package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class freeMarker {

	@Test
	public void test1() throws Exception{
		//1.创建一个模板文件
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存的目录和编码格式，一般是utf-8
		configuration.setDirectoryForTemplateLoading(new File("D:/professional_learning/Java/newcode/Project/Project2/yi2/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		configuration.setDefaultEncoding("utf-8");
		//3.加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("list.ftl");
		
		//4.创建一个数据集，可以是pojo，map  推荐使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker!");
		//创建一个pojo
		Student student = new Student(1, "sy1", 21, "上海");
		Student student2 = new Student(2, "sy2", 21, "上海");
		Student student3 = new Student(3, "sy3", 21, "上海");
		Student student4 = new Student(4, "sy4", 21, "上海");
		Student student5 = new Student(5, "sy5", 21, "上海");
		List<Student> list = new ArrayList<>();
		list.add(student);
		list.add(student2);
		list.add(student3);
		list.add(student4);
		list.add(student5);
		Date date = new Date();
		System.out.println(date);
		data.put("list", list);
		data.put("dateing", date);
		data.put("val", null);
		//5.创建一个Writer对象，指定输出文件的路径及文件名
		Writer out = new FileWriter(new File("D:/java/javaEE32/freemarker/list1.html"));
		//6.生成静态页面
		template.process(data, out); 
		//7.关闭流
		out.close();
	}
}
