package com.tonsincs.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.DataSources;
import com.tonsincs.main.JQ_Main;

/**
 * @ProjectName:JQueue
 * @ClassName: C3P0ConnentionProvider
 * @Description: TODO(连接池配置初始化的工具类)
 * @author 萧达光
 * @date 2014-5-28 下午01:27:36
 * 
 * @version V1.0
 */
public class C3P0ConnentionProvider {
	private static final String JDBC_DRIVER = "driverClass";
	private static final String JDBC_URL = "jdbcUrl";
	private static Logger log = Logger.getLogger(C3P0ConnentionProvider.class);
	private static DataSource ds;
	/**
	 * 初始化连接池代码块
	 */
	static {
		initDBSource();
	}

	/**
	 * 初始化c3p0连接池
	 */
	private static final void initDBSource() {
		log.info("开始初始化c3p0Pro连接池框架");
		Properties c3p0Pro = new Properties();
		try {
			
			// 加载配置文件
			FileInputStream in = new FileInputStream(JQ_Main.CONFIG_PATH
					+ "/c3p0.properties");
			c3p0Pro.load(in);
			in.close();
			log.info("加载c3p0Pro配置文件");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		String drverClass = c3p0Pro.getProperty(JDBC_DRIVER);
		if (drverClass != null) {
			try {
				// 加载驱动类
				Class.forName(drverClass);
				log.info("注册数据驱动类:" + drverClass);
			} catch (ClassNotFoundException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

		}

		Properties jdbcpropes = new Properties();
		Properties c3propes = new Properties();
		for (Object key : c3p0Pro.keySet()) {
			String skey = (String) key;
			if (skey.startsWith("c3p0.")) {
				c3propes.put(skey, c3p0Pro.getProperty(skey));
			} else {
				jdbcpropes.put(skey, c3p0Pro.getProperty(skey));
			}
		}
		log.info("初始化c3p0Pro配置文件完成");
		try {
			// 建立连接池
			DataSource unPooled = DataSources.unpooledDataSource(
					c3p0Pro.getProperty(JDBC_URL), jdbcpropes);
			ds = DataSources.pooledDataSource(unPooled, c3propes);
			log.info("初始c3p0Pro连接池完成");
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接对象
	 * 
	 * @return 数据连接对象
	 * @throws SQLException
	 */
	public static synchronized Connection getConnection() throws SQLException {
		final Connection conn = ds.getConnection();
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		log.info("从c3p0Pro连接池中取得连接数据库连接对象：" + conn);
		return conn;
	}
}
