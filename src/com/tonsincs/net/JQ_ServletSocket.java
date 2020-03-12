package com.tonsincs.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.task.ON_OFF_Thread;
import com.tonsincs.util.OS_Util;

/**
 * @ProjectName:JQueue
 * @ClassName: JQ_ServletSocket
 * @Description: TODO(排队系统服务Socket用于监控相关服务指令和 处理相关呼叫、 和LED屏显示的核心部分之一)
 * @author 萧达光
 * @date 2014-5-28 下午01:25:00
 * 
 * @version V1.0
 */
public class JQ_ServletSocket implements Runnable {

	private int defaultBindPort = Sys_Constant.LOCA_PORT; // 默认监听端口号为9001
	private int tryBindTimes = 0; // 初始的绑定端口的次数设定为0

	private ServerSocket serverSocket; // 服务套接字等待对方的连接和文件发送

	private ExecutorService executorService; // 线程池
	private final int POOL_SIZE = 2; // 单个CPU的线程池大小 4
	private static Logger log = Logger.getLogger(JQ_ServletSocket.class);
	private boolean running = true;
	private long outtime = Sys_Constant.SOCKET_TASK_OUT_TIME;

	public void setRunning(boolean running) {
		this.running = running;
	}

	// public static void main(String[] args) throws Exception {
	// JQ_ServletSocket js=new JQ_ServletSocket();
	// js.service();
	// }
	/**
	 * 不带参数的构造器，选用默认的端口号
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	public JQ_ServletSocket() throws Exception {
		try {
			this.bingToServerPort(defaultBindPort);
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors() * POOL_SIZE);
			System.out.println("开辟线程数 ： "
					+ Runtime.getRuntime().availableProcessors() * POOL_SIZE);
			log.info("排队机服务器，开辟线程数 ："
					+ Runtime.getRuntime().availableProcessors() * POOL_SIZE);
		} catch (Exception e) {
			// log.error("绑定端口不成功!可能原因是本地端口被其它程序占用，请检查一下");
			throw new Exception("绑定端口不成功!");
		}
	}

	public JQ_ServletSocket(int port) throws Exception {
		try {
			this.bingToServerPort(port);
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors() * POOL_SIZE);
			System.out.println("开辟线程数 ： "
					+ Runtime.getRuntime().availableProcessors() * POOL_SIZE);
			log.info("排队机服务器，开辟线程数 ："
					+ Runtime.getRuntime().availableProcessors() * POOL_SIZE);
		} catch (Exception e) {
			// log.error("绑定端口不成功!可能原因是本地端口被其它程序占用，请检查一下");
			throw new Exception("绑定端口不成功!");
		}
	}

	private void bingToServerPort(int port) throws Exception {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println(port);
			System.out.println("服务启动!");
			log.info("排队机侦听服务启动成功！");
			log.info("排队机服务端口：" + port);
		} catch (Exception e) {
			this.tryBindTimes = this.tryBindTimes + 1;
			port = port + this.tryBindTimes;
			if (this.tryBindTimes >= 20) {
				// log.error("排队机已经尝试很多次了，但是仍无法绑定到指定的端口!请重新选择绑定的默认端口号或者检查本地端口占用情况	");
				throw new Exception("您已经尝试很多次了，但是仍无法绑定到指定的端口!请重新选择绑定的默认端口号");
			}
			// 递归绑定端口
			this.bingToServerPort(port);
		}
	}

	/**
	 * @Title: service
	 * @Description: TODO(调用服务方法，服务启用线程池来处理每一个并发请求)
	 * @param
	 * @return void 返回类型
	 */
	@Deprecated
	public void service() {
		Socket socket = null;
		while (true) {
			try {
				socket = serverSocket.accept();
				executorService.execute(new Handler(socket));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket socket = null;
		while (running) {
			try {
				socket = serverSocket.accept();
				executorService.execute(new Handler(socket));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: respondPackage
	 * @Description: TODO(用于封装请求返回响应包)
	 * @param @param pg
	 * @param @return
	 * @return PG_Package 返回类型
	 */
	private PG_Package respondPackage(PG_Package pg) {
		int cmdID = 0; // 响应指令
		PG_Package pack = null; // 用于接收处理返回的结果
		switch (pg.getCmdID()) {
		case Sys_Constant.COUNTER_CALL: // 窗口叫号
			// 初始响应包的参数
			cmdID = Sys_Constant.COUNTER_CALL_REPLY;
			break;
		case Sys_Constant.COUNTER_PAUSE:// 窗口暂停
			cmdID = Sys_Constant.COUNTER_PAUSE_REPLY;
			break;
		case Sys_Constant.ON_OFF:// 关机/重启
			// 初始响应包的参数
			cmdID = Sys_Constant.ON_OFF_REPLY;
			break;
		case Sys_Constant.GET_SERVER_CONFIGURE: // 设置排管服务器参数
			cmdID = Sys_Constant.GET_SERVER_CONFIGURE_PEPLY;
			break;
		default:
			// System.out.println("无效指令");
			log.error("服务线程无法解释指令");
			break;
		}
		// 封装返回结果
		pack = new PG_Package(17, cmdID, 0, pg.getSerialNo(), 0);

		return pack;

	}

	/**
	 * @Title: doServetExecute
	 * @Description: TODO(接收处理相关服务器指令,并执行指令)
	 * @param @param pg
	 * @return PG_Package 返回类型
	 * @throws
	 */
	private void doServetExecute(PG_Package pg) {
		if (pg == null) {
			return;
		}
		String[] content = null; // 用于保存数据体内容
		String str = pg.getBody();
		content = str.split(Sys_Constant.DELIMITER);// 初始化参数数组
		switch (pg.getCmdID()) {
		case Sys_Constant.COUNTER_CALL: // 窗口叫号
			// 这个函数是向另叫号队列中产生一条记录（相当生产者）
			// OS_Util.counter_Call(content[1], content[2], content[3]);
			OS_Util.counter_Call1(content[1], content[2], content[3]);
			content = null;// 清空
			break;
		case Sys_Constant.COUNTER_PAUSE:// 窗口暂停
			// 这里要实现发送LED数据
			// OS_Util.send_LED(JQ_Main.OS_CONTEXT.get("PAUSE_SERVICE_CUE"),
			// content[1]);
			// OS_Util.counter_Pause(content[1]);
			OS_Util.counterNew_Pause(content[1]);
			// 初始响应包的参数
			content = null;
			break;
		case Sys_Constant.ON_OFF:// 关机/重启
			// 开启一条关机线程去执行关机或生重启任务(并且在程序上实现延时执行)
			new ON_OFF_Thread(Integer.parseInt(content[1]),
					Sys_Constant.SYS_TIME_DELAY).start();
			content = null;// 清空
			break;
		case Sys_Constant.GET_SERVER_CONFIGURE: // 设置排管服务器参数
			String[] key = { "CHANNEL_NO", "REMOTE_IP", "REMOTE_PORT" };
			OS_Util.updateOsContext(key, content);// 更新运行时的数据
			content = null;// 清空
			break;
		default:
			// System.out.println("无效指令");
			log.error("服务无法解释指令");
			break;
		}
	}

	/**
	 * @ClassName: Handler
	 * @Description: TODO(处理每次服务接收到的请求)
	 * @author 萧达光
	 * @date 2014-5-27 下午04:04:28
	 * @version V1.0
	 */
	private class Handler implements Runnable {
		private Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			log.info("排队机收到Socket请求：" + socket.getInetAddress() + "端口号:"
					+ socket.getPort() + "请求");
			System.out.println("排队机收到Socket请求：" + socket.getInetAddress()
					+ "端口号:" + socket.getPort() + "请求");
			DataInputStream dis = null;
			DataOutputStream dos = null;
			PG_Package c_pack = null;
			try {
				dis = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				dos = new DataOutputStream(new BufferedOutputStream(
						socket.getOutputStream()));
				long begingTime = System.currentTimeMillis();
				// 任务调度超时时间
				while ((System.currentTimeMillis() - begingTime < outtime)) {
					// 有收到数据才会处理
					if (dis.available() > 0) {
						// 开始解包
						int data_len = dis.readInt();
						byte[] data = new byte[data_len];
						dis.read(data);

						c_pack = SocketUtil.wrapup(data, data_len);
						log.info("排队机收到包:" + c_pack.toString());
						// System.out.println("排队机收到包:" + c_pack.toString());
						socket.shutdownInput();// 将输入流置为半关闭状态
						PG_Package pack = respondPackage(c_pack);// 组装响应返回包
						log.info("排队机服务响应排管系统包：" + pack);
						// System.out.println("排队机服务响应排管系统包：" + pack);
						dos.write(pack.getBuf());// 写数据
						dos.flush();

						data = null;
						break;
					}
				}
				socket.shutdownOutput();// 将输出流置为关闭状态
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			} finally {
				try {
					if (dos != null) {
						dos.close();
					}
					if (dis != null) {
						dis.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (c_pack != null) {
				// 执行相关排队机相关的接收到的指令处理
				doServetExecute(c_pack);// 执行关延后操作
			}

		}

	}

}
