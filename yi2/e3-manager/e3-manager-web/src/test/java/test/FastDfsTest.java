package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.util.FastDFSClient;

public class FastDfsTest {
	
	
	@Test
	public void testUpload() throws FileNotFoundException, IOException, MyException{
		//创建一个配置文件，文件名随意，内容是tracker的服务器地址
		//使用全局对象加载配置文件
		ClientGlobal.init("D:/professional_learning/Java/newcode/Project/Project2/yi2/e3-manager/e3-manager-web/src/main/resources/conf/client.conf");
		//TrackerClient
		TrackerClient trackerClient = new TrackerClient();
		//TrackerServer
		TrackerServer trackerServer = trackerClient.getConnection();
		//StorageServer
		StorageServer storageServer = null;
		//StorageClient
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//上传文件
		String[] s = storageClient.upload_appender_file("D:/java/java/a4.png", "png", null);
		for(String o : s){
			System.out.println(o);
		}
	}
	
	
	@Test
	public void test2() throws Exception{ 
		FastDFSClient fastDFSClient = new FastDFSClient("D:/professional_learning/Java/newcode/Project/Project2/yi2/e3-manager/e3-manager-web/src/main/resources/conf/client.conf");
		String s = fastDFSClient.uploadFile("D:/java/java/a5.png");
		System.out.println(s);
	}
}
