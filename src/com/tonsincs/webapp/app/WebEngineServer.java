package com.tonsincs.webapp.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.xml.sax.SAXException;

import com.tonsincs.main.JQ_Main;

/**
 * web 容器引擎服务
 * 
 * @author dellb
 */
public class WebEngineServer implements Runnable {

	private Logger log = Logger.getLogger(WebEngineServer.class);
	private String webapp;
	private Integer port;

	public WebEngineServer(String webapp, Integer port) {
		this.webapp = webapp;
		this.port = port;
	}

	@Override
	public void run() {
		Server server = null;
		WebAppContext context = null;
		try {
			port = port == null ? 8080 : port;
			// 服务器的监听端口
			server = new Server(port);
			// 关联一个已经存在的上下文
			context = new WebAppContext();
			// 设置描述符位置
			// context.setDescriptor("./config/web.xml");
			// 设置Web内容上下文路径
			context.setResourceBase(webapp);
			// 设置上下文路径
			context.setContextPath("/");
			context.setParentLoaderPriority(true);
			server.setHandler(context);
			// 启动
			server.start();
			server.join();
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (SAXException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (server != null) {
					server.stop();
				}
				server=null;
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}
