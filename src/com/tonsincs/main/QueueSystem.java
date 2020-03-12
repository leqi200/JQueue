package com.tonsincs.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.xml.sax.SAXException;

import com.tonsincs.net.JQ_ServletSocket;
import com.tonsincs.task.JQ_Heartbeat;
import com.tonsincs.task.MonitorThread;
import com.tonsincs.task.YiYinReadThread;
import com.tonsincs.webapp.app.WebEngineServer;

/**
 * @ProjectName:JQueue
 * @ClassName: QueueSystem
 * @Description: TODO(排队机核心框架)
 * @author 萧达光
 * @date 2014-5-28 下午01:36:43
 * 
 * @version V1.0
 */
public class QueueSystem {
	private Logger log = Logger.getLogger(QueueSystem.class);// 日志记录器的引用

	private WebUI ui;
	private Timer heartbeat = null;// 心跳时钟
	private Thread serverThread = null; // 服务线程
	private MonitorThread monitorThread = null;
	private Thread webEngineServerThread = null;

	public QueueSystem() throws Exception {
		init(); // 先执行一些系统基本的初始化操作
		ui = WebUI.getInstance();
		// ui.setSize(842, 602);
		ui.fullScreen();// 设置窗口全屏显示
		// 在eclipse中用这个
		// ui.setUrl(ui.getPath() + "/bin/dngj/test.html");
		// ui.setUrl("http://98.10.232.138:801");
		ui.setUrl(JQ_Main.OS_CONTEXT.get("OS_URL"));
		// ui.setUrl("http://www.baidu.com");

		// ui.setUrl(ui.getPath()+"/html/index.html");
		// System.out.println(JQ_Main.OS_CONTEXT.get("OS_URL"));
		ui.bind(SWT.MouseUp, new Listener() {
			public void handleEvent(Event e) {
				mouseUp(e);
			}
		});

		// 消息内循环
		ui.msgLoop();
	}

	/**
	 * @Title: init
	 * @Description: TODO(初始化方法)
	 * @param
	 * @return void 返回类型
	 * @throws Exception
	 */
	private void init() throws Exception {
		// 创建启动服务程序
		int port = Integer.parseInt(JQ_Main.OS_CONTEXT.get("LOCA_PORT"));

		serverThread = new Thread(new JQ_ServletSocket(port));
		serverThread.start();// 启动服务线程
		log.info("Socket服务程序初始化完成...");
		// -------------------系统定时器的初始化---------------------
		// 启动心跳定时器
		heartbeat = new Timer();
		// 获取心跳时间间隔
		int time_interval = Integer.parseInt(JQ_Main.OS_CONTEXT
				.get("TIME_INTERVAL"));
		// 如果为0则默认为一分钟执行一次
		time_interval = time_interval == 0 ? 1 : time_interval;
		// 延时1秒后启动
		heartbeat.schedule(new JQ_Heartbeat(), 1000,
				(time_interval * 1000 * 60));
		log.info("心跳程序初始化完成...");
		// 启动语音读取线程
		// new YiYinReadThread().start();
		// 获取通讯系统基本参数
		String monitor_ip = JQ_Main.OS_CONTEXT.get("MONITOR_SYSTEM_IP");
		int monitor_port = Integer.parseInt(JQ_Main.OS_CONTEXT
				.get("MONITOR_SYSTEM_PORT"));

		// 启动控制系统通信线程
		monitorThread = new MonitorThread(monitor_ip, monitor_port);
		monitorThread.start();
		log.info("启动控制系统通信线程完成...");

		// 启动服务web容器线程
		webEngineServerThread = new Thread(new WebEngineServer(
				JQ_Main.HTML_PATH, Integer.parseInt(JQ_Main.OS_CONTEXT
						.get("JETTY_PORT"))));
		webEngineServerThread.start();
		
		log.info("web容器引擎启动完成...");

	}

	protected void mouseUp(Event e) {
		String text = ui.getStatus();
 
		if (text.endsWith("#关闭")) {

			System.exit(0);
		} else if (text.endsWith("#最小化")) {
			ui.minScreen();
		} else {
			// TODO ...
		}
	}
}
