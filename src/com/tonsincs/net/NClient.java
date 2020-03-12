package com.tonsincs.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;

/**
 * @ProjectName:JQueue
 * @ClassName: NClient
 * @Description: TODO(用于建立与服务器通讯的客户端组件)
 * @author 萧达光
 * @date 2014-5-28 下午01:25:17
 * 
 * @version V1.0
 */
public class NClient {

	// 定义检测 SocketChannel 的Selector对象
	private Selector selector = null;
	// 定义处理编码和解码的字符集
	private Charset charset = Charset.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
	// 客户端 SocketChannel
	private SocketChannel sc = null;
	private String ip;
	private int port;

	/**
	 * <p>
	 * Title:构造方法
	 * </p>
	 * <p>
	 * Description:创建客户端通讯程序，与服务器进行通讯
	 * </p>
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public NClient(String ip, int port) throws IOException {
		super();
		this.ip = ip;
		this.port = port;
		init();
	}

	/**
	 * @Title: init
	 * @Description: TODO(初始化方法)
	 * @param @throws IOException
	 * @return void 返回类型
	 * @throws
	 */
	private void init() throws IOException {
		selector = Selector.open();
		InetSocketAddress isa = new InetSocketAddress(ip, port);
		// 调用 Open静态方法创建连接到指定主机的 SocketChannel
		sc = SocketChannel.open(isa);
		// 设置该sc 以非阻塞方式工作
		sc.configureBlocking(false);
		// 将SocketChannel 对象注册到指定Selector
		sc.register(selector, SelectionKey.OP_READ);
		System.out.println("创建客户端程序成功...");
		// 启动读取服务器端数据线程
		// new ClientThread().start();
		// // 创建键盘输入流
		// Scanner scan = new Scanner(System.in);
		// while (scan.hasNextLine()) {
		// // 读取键盘输入
		// String line = scan.nextLine();
		// System.out.println(charset.encode(line));
		// ByteBuffer bb = charset.encode(line);
		// System.out.println(bb);
		// // 将输入的内容输出到SocketChannel中
		// // sc.write(charset.encode(line));
		// ByteBuffer send = ByteBuffer.allocate(20 + 1 + bb.capacity());
		// send.putInt(send.capacity());
		// send.putInt(0x00000201);
		// send.putInt(0);
		// send.putInt(1);
		// send.putInt(0);
		// send.put(bb);
		// send.flip();
		// sc.write(send);
		// }

	}

	/**
	 * @Title: sendMsg
	 * @Description: TODO(将PG_Package封装的数据发送出去)
	 * @param @param pg
	 * @param @return
	 * @return PG_Package 返回类型
	 * @throws IOException
	 * @throws
	 */
	public PG_Package sendMsg(PG_Package pg) throws IOException {
		PG_Package pack = null; // 用于接收返回来的PG_Package数据
		if (pg != null) {
			// 根据PG包的长度创建一个只定长度的节点包
			ByteBuffer send = ByteBuffer.allocate(pg.getPktLength());
			send.putInt(send.capacity() - 4);
			send.putInt(pg.getCmdID());
			send.putInt(pg.getCmdStatus());
			send.putInt(pg.getSerialNo());
			send.putInt(pg.getReserverd());
			if (pg.getBody() == null || pg.getBody().isEmpty()) {
				// 如果为空则发送0字节结束
				send.put((byte) 0);
			} else {
				// 如果不为空、则将发送数据转换成字节数组再发送
				ByteBuffer bb = charset.encode(pg.getBody());
				send.put(bb);
				send.put((byte) 0);
			}
			// send.order(ByteOrder.BIG_ENDIAN); //这个是大端的

			// 重置数据指针
			send.flip();
			sc.write(send);
			// 侦听服务器响应的数据包
			pack = listen();
		}
		return pack;
	}

	/**
	 * @Title: listen
	 * @Description: TODO(侦听服务器返回来的数据并包成一个PG_Package对象)
	 * @param @return
	 * @return PG_Package 返回类型
	 * @throws IOException
	 */
	private PG_Package listen() throws IOException {
		boolean stop = false;
		PG_Package pack = null;
		int n = 0;
		int read = 0;
		try {
			// 轮询
			while (!stop) {
				if (selector.select() > 0) {// 获取Selector返回大于零，则有进行IO处理

					// 遍历每个有可用IO操作 Channel 对应的SelectionKey
					for (SelectionKey sk : selector.selectedKeys()) {
						// 删除正在处理的SelectionKey
						selector.selectedKeys().remove(sk);
						// 如果该SelectionKey对应的Channel中有可读的数据
						if (sk.isReadable()) {
							// 使用NIO　读取Channel中的数据
							SocketChannel sc = (SocketChannel) sk.channel();
							ByteBuffer buff = ByteBuffer.allocate(4);
							ByteBuffer data = null;
							String content = "";
							pack = new PG_Package();
							if (sc.read(buff) > 0) {
								sc.read(buff);// 这里有可能要调用下面的buff.flip()方法先对缓冲数组的指针初始化
								buff.flip();
								int len = buff.getInt(); // 获取报文长度
								data = ByteBuffer.allocate(len);
								sc.read(data);
								data.flip();
								// 设置包的长度
								pack.setPktLength(len);
								// 获取指令码
								pack.setCmdID(data.getInt());
								// 指令执行状态
								pack.setCmdStatus(data.getInt());
								// 流水号
								pack.setSerialNo(data.getInt());
								// 保留字段
								pack.setReserverd(data.getInt());
								// 判断当前读取的位置是否到达最后
								if (data.position() == data.capacity()
										|| (data.capacity() - data.position()) == 1) {
									pack.setBody(null);
								} else {
									// 获取主体内容
									content += charset.decode(data);
									System.out.println("打印当前客户端收到的数据体内容："
											+ content);
									pack.setBody(content);
								}
								System.out.println("封装PG实体:" + pack);
								// 设置标记停止读取
								stop = true;
								sk.interestOps(SelectionKey.OP_READ);
							}
						}
					}
				}
			}
		} finally {
			// 关闭Socket
			if (sc != null) {
				sc.close();
				System.out.println("关闭Socket通讯...");
			}
		}

		return pack;
	}

	// 定义读取服务器数据的线程
	private class ClientThread extends Thread {

		public void run() {
			try {
				while (selector.select() > 0) {
					// 遍历每个有可用IO操作 Channel 对应的SelectionKey
					for (SelectionKey sk : selector.selectedKeys()) {
						// 删除正在处理的SelectionKey
						selector.selectedKeys().remove(sk);
						// 如果该SelectionKey对应的Channel中有可读的数据
						if (sk.isReadable()) {
							// 使用NIO　读取Channel中的数据
							SocketChannel sc = (SocketChannel) sk.channel();
							ByteBuffer buff = ByteBuffer.allocate(4);
							ByteBuffer data = null;
							String content = "";
							if (sc.read(buff) > 0) {
								sc.read(buff);
								buff.flip();
								int len = buff.getInt(); // 获取报文长度
								data = ByteBuffer.allocate(len);
								sc.read(data);
								data.flip();
								// 获取指令码
								int cmdId = data.getInt();
								// 指令执行状态
								int cmdStatus = data.getInt();
								// 流水号
								int serialNo = data.getInt();
								// 保留字段
								int Reserverd = data.getInt();
								// 获取主体内容
								content += charset.decode(data);
								System.out.println();
							}

							// while (sc.read(buff) > 0) {
							// sc.read(buff);
							// buff.flip();
							// int aa= buff.getInt();
							// System.out.println(aa);
							// content += charset.decode(buff);
							// }
							// //解包
							//
							// // 打印输出读取的内容
							System.out.println("接收到服务器响应内容:" + content);
							// 为下一次读取作准备
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @Title: main
	 * @Description: TODO(测试服务程序主程序的入口)
	 * @param @param args
	 * @return void 返回类型
	 * @throws
	 */

	public static void main(String[] args) throws UnsupportedEncodingException {
		// try {
		// NClient nClient = new NClient("127.0.0.1", 7777);
		Charset charset = Charset.forName("GBK");
		// ByteBuffer bb = charset.encode("AC007~1");
		// nClient.sendMsg(new PG_Package(21 + bb.capacity(),
		// Sys_Constant.ON_OFF, 0, 1, 0, "AC007~1"));
		// // new NClient("127.0.0.1", 30000).init();
		//
		// } catch (IOException e) {
		// // TODO Autogenerated catch block
		// e.printStackTrace();
		// }
		// // 将字符转换成ASCII码值
		// // String str="abcde\0";
		// // char[] aa=str.toCharArray();
		// // char tp='\0';
		// // for (int i = 0; i < aa.length; i++) {
		// // System.out.println((int)aa[i]);
		// // }
		//
		// ByteBuffer a = charset.encode("你");
		// ByteBuffer b = ByteBuffer.allocate(21 + a.capacity());
		// b.putInt(21);
		// b.putInt(0x00000201);
		// b.putInt(0);
		// b.putInt(0);
		// b.put(a);
		// b.flip();
		// System.out.println(b);

		// Integer aa="你";
		// byte[] aaa="123".getBytes("GB2312");
		// System.out.println(aa.toHexString('你'));
		// byte[] cc=aa.getBytes("ISO-9500");
	}
}
