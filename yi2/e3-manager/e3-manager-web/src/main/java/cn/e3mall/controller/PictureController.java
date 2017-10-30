package cn.e3mall.controller;

import java.awt.PageAttributes.MediaType;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.util.FastDFSClient;
import cn.e3mall.common.util.JsonUtils;

@Controller
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		
		try{
			//1.
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			String e = uploadFile.getOriginalFilename();
			e.substring(e.lastIndexOf(".")+1);
			String r = fastDFSClient.uploadFile(uploadFile.getBytes(), e);
			String url = IMAGE_SERVER_URL + r;
			System.out.println(url);
			Map map = new HashMap<>();
			map.put("error",0);
			map.put("url", url);
//			return map;
			return JsonUtils.objectToJson(map);
		}catch(Exception e){
			e.printStackTrace();
			//5、返回map
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);

		}
		
	}
}
