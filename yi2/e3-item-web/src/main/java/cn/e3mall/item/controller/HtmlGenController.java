package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Controller
public class HtmlGenController {
	
	@Autowired
	FreeMarkerConfigurer free;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String free() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException{
		Configuration configuration = free.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		Writer out = new FileWriter(new File("D:/java/javaEE32/freemarker/test.txt"));
		Map map = new HashMap<>();
		map.put("hello", "loveksy");
		template.process(map, out);
		out.close();
		return "OK";
	}
}
