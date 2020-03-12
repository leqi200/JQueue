package com.tonsincs.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.task.ON_OFF_Thread;
import com.tonsincs.util.OS_Util;

/**
* @ProjectName:JQueue
* @ClassName: NServer
* @Description: TODO(服务器框架，是这个排队机的核心)
* @author 萧达光
* @date 2014-5-28 下午01:25:39
* 
* @version V1.0 
*/
public class NServer implements Runnable {
	// 用于检测Channel 状态的Selector
	private Selector selector = null;
	// 定义实现编码、解码的字符集对象
	private Charset charset = Charset.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
	// private String ip;// IP
	private int port;// 端号号
	private ServerSocketChannel server = null;
	public boolean stop = false;
	private static Logger log = Logger.getLogger(NServer.class);

	/**
	 * <p>
	 * Title: 创建服务器框架构造函数
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param port
	 *            侦听端口号
	 * @throws IOException
	 */
	public NServer(int port) throws IOException {
		super();
		this.port = port;
	}

	/**
	 * @Title: init
	 * @Description: TODO(服务初始化方法)
	 * @param @throws IOException
	 * @return void 返回类型
	 * @throws
	 */
	private void init() {
		try {
			selector = Selector.open();
			// 通过open 方法来打开一个未绑定的ServerSocketChannel 实例
			server = ServerSocketChannel.open();
			InetSocketAddress isa = new InetSocketAddress(this.port);
			// 将该ServerSocketChannel 绑定到指定IP地址
			server.socket().bind(isa);
			// 设置ServerSocket以非阻塞方式工作
			server.configureBlocking(false);
			// 将Server注册到指定Selector 对象
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务器启动成功...");
			log.info("服务器启动成功...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * @Title: doServetExecute
	 * @Description: TODO(处理相关服务器指令,并执行指令)
	 * @param @param pg
	 * @return PG_Package 返回类型
	 * @throws
	 */
	private PG_Package doServetExecute(PG_Package pg) {
		if (pg == null) {
			return null;
		}
		int cmdID = 0; // 响应指令
		PG_Package pack = null; // 用于接收处理返回的结果
		String[] content = null; // 用于保存数据体内容
		String str = pg.getBody().substring(0, pg.getBody().length() - 1);
		log.info("打印服务器接收到的请求数据体：" + str);
		content = str.split(Sys_Constant.DELIMITER);// 初始化参数数组
		switch (pg.getCmdID()) {
		case Sys_Constant.COUNTER_CALL: // 窗口叫号
			// 这里要实现发送LED数据并且产生语音
			/*
			 * .......
			 */
			OS_Util.counter_Call(content[1], content[2], content[3]);
			content = null;// 清空
			// 初始响应包的参数
			cmdID = Sys_Constant.COUNTER_CALL_REPLY;
			break;
		case Sys_Constant.COUNTER_PAUSE:// 窗口暂停
			// 这里要实现发送LED数据
			OS_Util.send_LED(JQ_Main.OS_CONTEXT.get("PAUSE_SERVICE_CUE"), content[1]);
			// 初始响应包的参数
			cmdID = Sys_Constant.COUNTER_PAUSE_REPLY;
			content = null;
			break;
		case Sys_Constant.ON_OFF:// 关机/重启
			// 初始响应包的参数
			cmdID = Sys_Constant.ON_OFF_REPLY;
			//开启一条关机线程去执行关机或生重启任务(并且在程序上实现延时执行)
			 new ON_OFF_Thread(Integer.parseInt(content[1]),
				Sys_Constant.SYS_TIME_DELAY).start();
			content = null;// 清空
			break;
		case Sys_Constant.GET_SERVER_CONFIGURE: // 设置排管服务器参数
			String[] key = { "CHANNEL_NO", "REMOTE_IP", "REMOTE_PORT" };
			OS_Util.updateOsContext(key, content);// 更新运行时的数据
			cmdID = Sys_Constant.GET_SERVER_CONFIGURE_PEPLY;
			content = null;// 清空
			break;
		default:
			System.out.println("无效指令");
			log.error("服务线程无法解释指令");
			break;
		}
		// 封装返回结果
		pack = new PG_Package(21, cmdID, 0, pg.getSerialNo(), 0);

		return pack;
	}

	/**
	 * @Title: main
	 * @Description: TODO(测试服务程序主程序的入口)
	 * @param @param args
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		try {
			new Thread(new NServer(9001)).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 处理相关服务业务逻辑
	public void run() {
		init();// 调用初始化方法
		while (!stop) {
			try {
				if (selector.select() > 0) {
					// 依次处理 Selector 上的每个已选择的 SelectionKey
					for (SelectionKey sk : selector.selectedKeys()) {
						// 从 Selector 上的已选择Key 集合中删除正在处理的SelectionKey
						selector.selectedKeys().remove(sk);
						// 如果 sk 对应的通道包含客户端的连接请求
						if (sk.isAcceptable()) {
							// 调用accept 方法接受连接，产生服务器端对应的SocketChannel
							SocketChannel sc = server.accept();
							// 设置采用非阻塞模式
							sc.configureBlocking(false);
							// 将该 SocketChannel 注册到Selector
							sc.register(selector, SelectionKey.OP_READ);
							// 将 sk 对应 的Channel 设置成准备接受其他请求
							sk.interestOps(SelectionKey.OP_ACCEPT);
						}
						// 如果 sk对应的通道有数据需要读取
						if (sk.isReadable()) {
							// 获取该 SelectionKey 对应的Channel，该Channel中有可读的数据
							SocketChannel sc = (SocketChannel) sk.channel();
							PG_Package pg = null;
							try {
								pg = getWrap(sc, sk); // 调用包裹方法
								System.out.println("服务器收到的数据" + pg);
							} catch (IOException e) {
								// TODO:
								// 如果捕捉到Sk对应的Channel出现了异常，表明该Channel对应的Client
								// 出现了问题，所以从Selector中取消sk的注册
								sk.cancel();
								if (sk.channel() != null) {
									sk.channel().close();
								}

							}
							// System.out.println(pg);
							// 获取上面的内容，再次进行相关的业务处理区域
							if (pg.getBody() != null
									&& pg.getBody().length() > 0) {
								// 把接收到的内容进行相关处理并且响应客户端
								// 调用处理相关服务器指令,并执行指令
								PG_Package pack = doServetExecute(pg);
								ByteBuffer data = pg_PackageToByteBuffer(pack);
								// 响应客户端
								sc.write(data);
								//同时关闭与客户端的连接
								sk.cancel();
								if (sk.channel() != null) {
									sk.channel().close();
								}
							}
						}
					}
				}
			} catch (ClosedChannelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}

	}

	/**
	 * @Title: pg_PackageToByteBuffer
	 * @Description: TODO(将包信息转换成ByteBuffer对象)
	 * @param @param pg 排管包结构
	 * @param @return
	 * @return ByteBuffer 返回类型
	 */
	public ByteBuffer pg_PackageToByteBuffer(PG_Package pg) {
		if (pg != null) {
			ByteBuffer buff = ByteBuffer.allocate(pg.getPktLength());
			buff.putInt(pg.getPktLength());
			buff.putInt(pg.getCmdID());
			buff.putInt(pg.getCmdStatus());
			buff.putInt(pg.getSerialNo());
			buff.putInt(pg.getReserverd());

			if (pg.getBody() == null || pg.getBody().equals("")
					|| pg.getBody().isEmpty()) {
				// 如果为空则发送0字节结束
				buff.put((byte) 0);
			} else {
				// 有数据体则发送
				buff.put(charset.encode(pg.getBody()));
				buff.put((byte) 0);
			}
			// 重置数据指针
			buff.flip();
			return buff;
		}
		return null;

	}

	/**
	 * @Title: getWrap
	 * @Description: TODO(用于把字节数据包裹成PG_Package 对象返回)
	 * @param @param sc
	 * @param @param sk
	 * @param @return
	 * @return PG_Package 返回类型
	 * @throws IOException
	 */
	public PG_Package getWrap(SocketChannel sc, SelectionKey sk)
			throws IOException {
		// 定义准备执行读取数据的ByteBuffer
		ByteBuffer buff = ByteBuffer.allocate(4);// 用于取包的长度
		ByteBuffer data = null;
		String content = "";
		PG_Package pg = null; // 用于封装排管系统的数据包
		// 开始读取数据
		if (sc.read(buff) > 0) {
			sc.read(buff);
			buff.flip();
			// 获取报文长度
			int len = buff.getInt();
			data = ByteBuffer.allocate(len);// 创建指定长度的字节缓冲数组
			sc.read(data);
			data.flip(); // 重置数据指针位置

			pg = new PG_Package();
			// 设置长度
			pg.setPktLength(len);
			// 获取指令码
			pg.setCmdID(data.getInt());
			// 指令执行状态
			pg.setCmdStatus(data.getInt());
			// 流水号
			pg.setSerialNo(data.getInt());
			// 保留字段
			pg.setReserverd(data.getInt());
			// 判断当前读取的位置是否到达最后
			if (data.position() == data.capacity()
					|| (data.capacity() - data.position()) == 1) {
				pg.setBody(null);
			} else {
				// 获取主体内容
				content += charset.decode(data);
				System.out.println("打印当前服务接收到的数据体内容：" + content);
				pg.setBody(content);
			}
			// 将sk 对应的Channel 设置成准备下一次读取
			sk.interestOps(SelectionKey.OP_READ);
		}
		return pg;
	}
}
