package com.tonsincs.main;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.tonsincs.net.NServer;
import com.tonsincs.task.JQ_Heartbeat;
import com.tonsincs.util.SQLHelper;

/**
 * @ProjectName:JQueue
 * @ClassName: JQ_Main
 * @Description: TODO(排队机系统主程序入口)
 * @author MrXiao
 * @date 2015-5-18 下午14:00:01
 * @version V2.0
 */
public class JQ_Main {
	public static Map<String, String> OS_CONTEXT; // 用于维护系统上下信息对象
	public static Map<String, String> RUN_TEMPORARY;// 用于保存排队系统运行时的临时变量
	private static Logger log = Logger.getLogger(JQ_Main.class);// 创建日志记录器
	public static String ROOT_PATH = null; // 系统运行的根据目录
	public static String CONFIG_PATH = null;// 系统配置目录
	public static String HTML_PATH = null; // 系统存放HTML页面目录
	public static String LOGS_PATH = null;// 系统日志存放目录
	public static String YUYIN_PATH = null;// 语音文件存放的目录

	static {
		init();
	}

	public static void main(String[] args) {

		try {
			// 启动排队系统主系统
			new QueueSystem();
		} catch (Exception e) {
			log.error("", e);
		}

	}

	/**
	 * 初始化上下文信息
	 */
	private final static void init() {
		// 创建系统全局上下文信息保存
		OS_CONTEXT = new HashMap<String, String>();
		// 创建临时用于存放更新信息的集合
		RUN_TEMPORARY = new HashMap<String, String>();

		// 初始化系统路径
		initPath();
		// 初始化日志记录器
		initLog4j();
		// 读取基础配置文件信息，初始化排队机上下文
		initOSContext();
		// 初始化语音参数
		//initYuYin();

		// 打印提示信息
		log.info("打印系统上下文信息" + OS_CONTEXT);
	}

	/**
	 * 初始化系统路径并且指定资源目录路径
	 */
	private final static void initPath() {
		ROOT_PATH = System.getProperty("user.dir");
		CONFIG_PATH = ROOT_PATH + "/config";
		HTML_PATH = ROOT_PATH + "/html";
		LOGS_PATH = ROOT_PATH + "/logs";
		YUYIN_PATH = ROOT_PATH + "/yuyin/YUEYU";

		CONFIG_PATH = CONFIG_PATH.replace("\\", "/");
		HTML_PATH = HTML_PATH.replace("\\", "/");
		LOGS_PATH = LOGS_PATH.replace("\\", "/");
		YUYIN_PATH = YUYIN_PATH.replace("\\", "/");
	}

	/**
	 * 初始化Log4j日志记录器
	 */
	private final static void initLog4j() {
		File log4j_F = new File(CONFIG_PATH + "/log4j.properties");
		if (log4j_F.exists()) {
			System.setProperty("JQueue2.0", ROOT_PATH);
			// System.out.println(log4j_F.getAbsolutePath());
			// 初始化LOG4J 配 信息
			PropertyConfigurator.configure(log4j_F.getAbsolutePath());
		}
	}

	/**
	 * 初始化程序上下文件全局参数信息
	 */
	private final static void initOSContext() {

		// 读取系统配置信息(jq.properties)
		FileInputStream in = null;
		List<Object> list = null;
		Properties os = new Properties();
		try {
			log.info("读取 [" + CONFIG_PATH + "/jq.properties ] 配置文件...");
			in = new FileInputStream(CONFIG_PATH + "/jq.properties");
			os.load(in);

			// 读取配置信息到内存中
			Enumeration os_key = os.propertyNames();
			while (os_key.hasMoreElements()) {
				String name = (String) os_key.nextElement();
				OS_CONTEXT.put(name, os.getProperty(name));
			}
			Thread.sleep(1500 * 10);// 延迟20秒
			log.info("读取jq.properties 配置文件完成" + OS_CONTEXT);

			// 读取数据库配置参数
			SQLHelper sql_help = new SQLHelper();
			log.info("读取数据库参配置数信息");
			list = sql_help.query(OS_CONTEXT.get("QUEUE_ALLPARAMETER_SQL"));
			for (Object obj : list) {
				Object[] c = (Object[]) obj;
				OS_CONTEXT.put((String) c[1], (String) c[2]);
			}
			log.info("初始化数据库参数配置信息完成");

			// 释放引用配置文件
			in.close();
			in = null;
			// log.info("加载jq.properties配置文件...");
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} catch (InterruptedException e) {
			log.error("", e);
		} catch (SQLException e) {
			log.error("", e);
		}

	}

	/**
	 * 初始化语音参数
	 */
	private final static void initYuYin() {
		log.info("初始化语音参数");
		//System.out.println(YUYIN_PATH);
		// 初始化系统语音文件
		File f_YuYin = new File(OS_CONTEXT.get("YUYINPATH"));
		log.info("打印语音文件路径:" + f_YuYin.getAbsolutePath());
		if (f_YuYin.isDirectory()) {
			File[] files = f_YuYin.listFiles();
			for (int i = 0; i < files.length; i++) {
				// System.out.println(files[i].toString());
				String fileName = files[i].getName();
				OS_CONTEXT.put(fileName.substring(0, fileName.length() - 4),
						files[i].getPath());
			}

		} else {
			log.info("指定语音路径不是一个合法的目录结构");
		}
	}

}
