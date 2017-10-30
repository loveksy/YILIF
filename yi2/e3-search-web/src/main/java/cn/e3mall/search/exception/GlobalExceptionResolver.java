package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("debug...");
		logger.info("info...");
		logger.warn("warn...");
		logger.error("系统发生异常", ex);
		//发邮件
		//使用jmail工具包  发短信使用第三方的webservice
		
		//显示错误页面
		ModelAndView model = new ModelAndView();
		model.setViewName("error/exception");
		return model;
	}

}