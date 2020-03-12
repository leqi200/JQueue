package com.tonsincs.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;

/**
 * @ProjectName:JQueue
 * @ClassName: JQ_ClientSocket
 * @Description: TODO(这个组件主要负责每次建立TCP短连接 这是一个客户端Socket组件)
 * @author 萧达光
 * @date 2014-5-28 下午01:24:32
 * 
 * @version V1.0.1
 */
public class JQ_ClientSocket {

	private String ip;
	private int port;
	private int timeOut;
	private Socket socket;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	private static Logger log = Logger.getLogger(JQ_ClientSocket.class);

	/**
	 * 构造函数
	 * 
	 * @param ip目标IP
	 * @param port目标端口号
	 * @param timeOut设置超时时间
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public JQ_ClientSocket(String ip, int port, int timeOut)
			throws UnknownHostException, IOException {
		super();
		this.ip = ip;
		this.port = port;
		this.timeOut = timeOut;
		init();
	}

	/**
	 * @Title: init
	 * @Description: TODO(初始Socket服务)
	 * @param @throws UnknownHostException
	 * @param @throws IOException
	 * @return void 返回类型
	 * @throws
	 */
	public void init() throws UnknownHostException, IOException {
		// 建立服务器连接
		socket = new Socket(ip, port);
		socket.setSoTimeout(timeOut);

		// 获取相应的输入/输出流
		dos = new DataOutputStream(new BufferedOutputStream(
				socket.getOutputStream()));
		dis = new DataInputStream(new BufferedInputStream(
				socket.getInputStream()));
		log.info("创建Socket客户端成功");
	}

	/**
	 * @Title: sendMsg
	 * @Description: TODO(用于发送消息到排管系统)
	 * @param @param pgPackage
	 * @param @return
	 * @param @throws UnknownHostException
	 * @param @throws IOException
	 * @return String 如果成功则返回组装好的内容
	 * @throws
	 */
	public PG_Package sendMsg(PG_Package pg) {
		PG_Package pack = null;
		long timeout = 3000;
		try {
			if (pg.getBuf() == null) {
				throw new Exception("发送包的字节包数组为空...");
			}
			System.out.println("发送数据包:" + pg.toString());
			log.info("发送数据包:" + pg.toString());
			dos.write(pg.getBuf());
			dos.flush();
			// 半关闭状态关闭输出流，标识当前状态不可以再输出
			socket.shutdownOutput();
			Thread.sleep(200);
			
			long beginTime = System.currentTimeMillis();
			int data_len = 0;// 记录读取数据的总长度
			while ((System.currentTimeMillis() - beginTime < timeout)) {
				if (dis.available() > 0) {
					data_len = dis.readInt();// 获取包头长度
					byte[] data = new byte[data_len];
					dis.read(data);

					// 开始解包操作
					pack = SocketUtil.wrapup(data, data_len);
					log.info("收到回复包：" + pack.toString());
					System.out.println("收到回复包：" + pack.toString());
					data = null;
					break;
				}
			}
			// 半关闭状态关闭输入流，标识当前状态不可以再读取数据
			socket.shutdownInput();

		} catch (UnknownHostException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (dos != null) {
					dos.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				log.error("", e);
			}
		}
		return pack;
	}

//	public static void main(String[] args) {
//		// String aa="0x00000201~AAGZ00001";
//		// System.out.println(Integer.parseInt("17", 16));
//		// byte [] aaa=aa.getBytes();
//		// System.out.println(aa.getBytes());
//		// ByteBuffer buffer=ByteBuffer.allocate(10000);
//		try {
//			// JQ_ClientSocket jc = new JQ_ClientSocket("10.244.28.93", 8500,
//			// 10000);
//			JQ_ClientSocket jc = new JQ_ClientSocket("10.244.28.93", 8500,
//					10000);
//			// String senStr = "TEST00001";
//			String senStr = "AAGZ10B13~13600029211~A";// 13822228888
//			byte[] sendata = senStr.getBytes();
//			int len = 16 + 1 + sendata.length;// 16是不带存放包长度 的加1 是最后一个字节为空，用于存放
//			PG_Package pg = new PG_Package(len, 0x00000203, 0, 0, 0, senStr);// 这个是数据包的定义
//			PG_Package pgg = jc.sendMsg(pg);
//			System.out.println(pgg);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch\] block
//			e.printStackTrace();
//		}
//	}
}
