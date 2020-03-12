package com.tonsincs.webapp.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.xml.sax.SAXException;

public class JettyMain {

	public static void main(String[] args) {
		try {
			// 服务器的监听端口
			Server server = new Server(8080);
			// 关联一个已经存在的上下文
			WebAppContext context = new WebAppContext();
			System.out.println(System.getProperty("user.dir"));
			String root = System.getProperty("user.dir");
			String webapp = (root + "/html").replace("\\", "/");
			// 设置描述符位置
			//context.setDescriptor("./config/web.xml");
			// 设置Web内容上下文路径
			context.setResourceBase(webapp);
			// 设置上下文路径
			context.setContextPath("/");
			context.setParentLoaderPriority(true);
			server.setHandler(context);
			// 启动
			server.start();
			server.join();

			System.out.println("启动服务会看不到这段内容");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
