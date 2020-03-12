package com.tonsincs.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import javax.comm.SerialPort;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.net.NServer;
import com.tonsincs.util.OS_Util;
import com.tonsincs.util.SendLEDMessage;
 
/**
* @ProjectName:JQueue
* @ClassName: GeneralProcedure
* @Description: TODO(测试工具程序，供调试测试用)
* @author 萧达光
* @date 2014-5-28 下午01:35:42
* 
* @version V1.0 
*/
public class GeneralProcedure extends Shell {
	private Text txt_phoneNo;
	private Text txt_IP;
	private Text txt_sport;
	private Text txt_LEDContent;
	private Text txt_LED;
	private Text txt_windowNo;
	private Text txt_queueNumber;
	private Text txt_channelNo;
	private Text text;
	private Text txt_locaport;
	private GeneralServer gserver;
	private Text txt_serveMonitor;
	private Text txt_clientMsg;
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			GeneralProcedure shell = new GeneralProcedure(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public GeneralProcedure(Display display) {
		super(display, SWT.BORDER | SWT.CLOSE);

		Group group = new Group(this, SWT.NONE);
		group.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		group.setText("基本参数配置");
		group.setBounds(10, 0, 628, 222);

		Label lblvip = new Label(group, SWT.NONE);
		lblvip.setBounds(20, 26, 102, 23);
		lblvip.setText("手机或VIP卡号码：");

		txt_phoneNo = new Text(group, SWT.BORDER);
		txt_phoneNo.setText("12345678901");
		txt_phoneNo.setBounds(139, 23, 290, 23);

		Label lblip = new Label(group, SWT.NONE);
		lblip.setText("服务IP地址：");
		lblip.setBounds(231, 50, 71, 23);

		txt_IP = new Text(group, SWT.BORDER);
		txt_IP.setText("10.244.28.93");
		txt_IP.setBounds(312, 50, 117, 23);

		Label label_2 = new Label(group, SWT.NONE);
		label_2.setText("服务器端口号：");
		label_2.setBounds(235, 89, 71, 23);

		txt_sport = new Text(group, SWT.BORDER);
		txt_sport.setText("8500");
		txt_sport.setBounds(316, 89, 117, 23);

		txt_LEDContent = new Text(group, SWT.BORDER);
		txt_LEDContent.setText("请A001到4号窗口");
		txt_LEDContent.setBounds(316, 124, 117, 23);

		Label lblled = new Label(group, SWT.NONE);
		lblled.setText("发送内容LED：");
		lblled.setBounds(235, 124, 71, 23);

		txt_LED = new Text(group, SWT.BORDER);
		txt_LED.setText("4");
		txt_LED.setBounds(87, 89, 90, 23);

		Label lblLed = new Label(group, SWT.NONE);
		lblLed.setText("LED屏地址：");
		lblLed.setBounds(20, 89, 61, 23);

		Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("串口端号：");
		label_4.setBounds(20, 124, 61, 23);

		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("窗口号：");
		label_1.setBounds(22, 173, 61, 23);

		txt_windowNo = new Text(group, SWT.BORDER);
		txt_windowNo.setText("2");
		txt_windowNo.setBounds(87, 173, 125, 23);

		Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("排队号码：");
		label_3.setBounds(235, 173, 71, 23);

		txt_queueNumber = new Text(group, SWT.BORDER);
		txt_queueNumber.setText("A001");
		txt_queueNumber.setBounds(316, 173, 138, 23);

		Label label_5 = new Label(group, SWT.NONE);
		label_5.setText("营业厅代号：");
		label_5.setBounds(435, 26, 73, 23);

		txt_channelNo = new Text(group, SWT.BORDER);
		txt_channelNo.setText("GZTEST523");
		txt_channelNo.setBounds(512, 23, 102, 23);

		Label label_6 = new Label(group, SWT.NONE);
		label_6.setAlignment(SWT.RIGHT);
		label_6.setText("号屏");
		label_6.setBounds(183, 89, 35, 23);

		final Combo cob_SerialPort = new Combo(group, SWT.READ_ONLY);
		cob_SerialPort.setItems(new String[] { "COM1", "COM2", "COM3", "COM4",
				"COM5", "COM6", "COM7" });
		cob_SerialPort.setBounds(87, 124, 125, 25);
		cob_SerialPort.select(0);

		Label lblip_1 = new Label(group, SWT.NONE);
		lblip_1.setText("本机当前IP：");
		lblip_1.setBounds(435, 53, 73, 23);

		text = new Text(group, SWT.BORDER);
		text.setBounds(512, 51, 102, 23);

		Label label_7 = new Label(group, SWT.NONE);
		label_7.setText("本地服务端口");
		label_7.setBounds(437, 92, 71, 23);

		txt_locaport = new Text(group, SWT.BORDER);
		txt_locaport.setText("8888");
		txt_locaport.setBounds(512, 86, 102, 23);

		final Button rbtn_typephon = new Button(group, SWT.RADIO);
		rbtn_typephon.setSelection(true);
		rbtn_typephon.setBounds(34, 55, 61, 17);
		rbtn_typephon.setText("手机号");

		Button rbtn_vipcard = new Button(group, SWT.RADIO);
		rbtn_vipcard.setText("VIP卡");
		rbtn_vipcard.setBounds(117, 56, 61, 17);

		Group group_1 = new Group(this, SWT.NONE);
		group_1.setText("操作");
		group_1.setBounds(10, 257, 284, 169);

		Button btn_GSM = new Button(group_1, SWT.NONE);

		btn_GSM.setText("GSM 新用户");
		btn_GSM.setBounds(6, 38, 80, 27);

		Button btn_3G = new Button(group_1, SWT.NONE);
		btn_3G.setEnabled(false);

		btn_3G.setBounds(98, 38, 80, 27);
		btn_3G.setText("3G 新入网");

		Button btn_getParam = new Button(group_1, SWT.NONE);

		btn_getParam.setText("取参数设置");
		btn_getParam.setBounds(183, 38, 80, 27);

		Button btn_SendLED = new Button(group_1, SWT.NONE);

		btn_SendLED.setText("发送内容LED");
		btn_SendLED.setBounds(6, 132, 80, 27);

		Button btn_updatip = new Button(group_1, SWT.NONE);

		btn_updatip.setText("修改本机IP");
		btn_updatip.setBounds(98, 132, 80, 27);

		final Button btn_startServe = new Button(group_1, SWT.NONE);

		btn_startServe.setText("启动服务");
		btn_startServe.setBounds(98, 85, 80, 27);

		final Button btn_closeServe = new Button(group_1, SWT.NONE);
		btn_closeServe.setEnabled(false);

		btn_closeServe.setText("关闭服务");
		btn_closeServe.setBounds(183, 85, 80, 27);

		Button btn_new = new Button(group_1, SWT.NONE);

		btn_new.setText("新用户取号");
		btn_new.setBounds(6, 85, 80, 27);

		Button btn_call = new Button(group_1, SWT.NONE);

		btn_call.setText("呼叫");
		btn_call.setBounds(183, 132, 80, 27);

		Group group_2 = new Group(this, SWT.NONE);
		group_2.setText("服务接收到的数据");
		group_2.setBounds(309, 228, 327, 124);
		
		txt_serveMonitor = new Text(group_2, SWT.BORDER | SWT.V_SCROLL);
		txt_serveMonitor.setBounds(10, 18, 307, 96);

		Group group_3 = new Group(this, SWT.NONE);
		group_3.setText("响应结果");
		group_3.setBounds(309, 358, 327, 99);
		
		txt_clientMsg = new Text(group_3, SWT.BORDER | SWT.V_SCROLL);
		txt_clientMsg.setBounds(10, 21, 307, 68);

		// 绑定事件

		// GSM按钮事件
		btn_GSM.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int number_Type;

				if (rbtn_typephon.getSelection()) {
					number_Type = 1;
				} else {
					number_Type = 2;
				}
				String ip = txt_IP.getText();
				int port = Integer.parseInt(txt_sport.getText());
				String number = txt_phoneNo.getText();
				String channel_no = txt_channelNo.getText();// 获取渠道号
				String send_data = channel_no + Sys_Constant.DELIMITER
						+ number_Type + Sys_Constant.DELIMITER + number;
				// 创建字符集
				Charset charset = Charset
						.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
				// 初始化提交包参数
				PG_Package pg = new PG_Package();
				pg.setCmdID(Sys_Constant.GET_BIZ_MENU);
				pg.setCmdStatus(0);
				pg.setSerialNo(1);
				pg.setReserverd(0);
				pg.setBody(send_data);
				pg.setPktLength(20 + 1 +charset.encode(send_data).capacity());

				final PG_Package pack = OS_Util
						.commsend_PG_System(pg, ip, port);
				Display display = Display.getDefault();
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						txt_clientMsg.setText("");
						txt_clientMsg.setText(pack.toString());
						
					}
				});
			}
		});
		// 3G按钮事件
		btn_3G.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		// 获取系统参数按钮事件
		btn_getParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 用于接收服务器返包数据

				PG_Package req_pg = new PG_Package();
				String ip = txt_IP.getText();
				int port = Integer.parseInt(txt_sport.getText());
				String channelNo =txt_channelNo.getText(); //JQ_Main.OS_CONTEXT.get("CHANNEL_NO");
				Charset CHARSET = Charset.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
				// 初始化包参数
				// 包的长度[20+数据体字符长度+1(0结束符的1个字节)] 
				req_pg.setPktLength(20+ 1 + CHARSET.encode(channelNo)
						.capacity());
				req_pg.setCmdID(Sys_Constant.GET_PARAMETER);
				req_pg.setCmdStatus(0);
				req_pg.setSerialNo(1);
				req_pg.setBody(channelNo);

				// 发送数据
				final PG_Package rep_pg = OS_Util.commsend_PG_System(req_pg,
						ip, port);
				Display display = Display.getDefault();
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						txt_clientMsg.setText("");
						txt_clientMsg.setText(rep_pg.toString());
					}
				});
			}
		});

		// 新用户处理事件
		btn_new.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int number_Type;

				if (rbtn_typephon.getSelection()) {
					number_Type = 1;
				} else {
					number_Type = 2;
				}
				String ip = txt_IP.getText();
				int port = Integer.parseInt(txt_sport.getText());
				String number = "新入网用户";
				String channel_no = txt_channelNo.getText();// 获取渠道号
				String send_data = channel_no + Sys_Constant.DELIMITER
						+ number_Type + Sys_Constant.DELIMITER + number;
				// 创建字符集
				Charset charset = Charset
						.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
				// 初始化提交包参数
				PG_Package pg = new PG_Package();
				pg.setCmdID(Sys_Constant.GET_BIZ_MENU);
				pg.setCmdStatus(0);
				pg.setSerialNo(1);
				pg.setReserverd(0);
				pg.setBody(send_data);
				pg.setPktLength(charset.encode(send_data).capacity());

				final PG_Package pack = OS_Util
						.commsend_PG_System(pg, ip, port);
				Display display = Display.getDefault();
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						txt_clientMsg.setText("");
						txt_clientMsg.setText(pack.toString());
					}
				});

			}
		});
		// 启动服务
		btn_startServe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int locaport = Integer.parseInt(txt_locaport.getText());
				int port=Integer.parseInt(txt_locaport.getText());
				gserver=new GeneralServer(port);
				gserver.start();
				// lb_serveMonitor.setText("");
				// lb_serveMonitor.setText("服务器启动成功...");
				txt_serveMonitor.setText("");
				txt_serveMonitor.setText("服务器启动成功...");
				btn_startServe.setEnabled(false);
				btn_closeServe.setEnabled(true);
			}
		});

		// 关闭服务
		btn_closeServe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_startServe.setEnabled(true);
				btn_closeServe.setEnabled(false);
				try {
					gserver.selector.close();
					gserver.server.close();
					gserver.stop=true;
					gserver=null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				

				// lb_serveMonitor.setText("");
				// lb_serveMonitor.setText("服务器关闭...");
				txt_serveMonitor.setText("");
				txt_serveMonitor.setText("服务器关闭成功...");
			}
		});

		// 发送内容到LED屏显示
		btn_SendLED.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String counterNo = txt_windowNo.getText();
				String content = txt_LEDContent.getText();
				OS_Util.send_LED(counterNo, content);
			}
		});
		// 修改本机IP
		btn_updatip.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		// 呼叫按钮处理事件
		btn_call.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String counterNo = txt_windowNo.getText();// 窗口号
				String serviceNo = "A";// 业务编号
				String queueNumber = txt_queueNumber.getText();// 排队号码
				String portName=cob_SerialPort.getText();
			
				OS_Util.counter_Call(counterNo, serviceNo, queueNumber);
			}
		});

		createContents();
	}

	class GeneralServer extends Thread {
		// 用于检测Channel 状态的Selector
		private Selector selector = null;
		// 定义实现编码、解码的字符集对象
		private Charset charset = Charset
				.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
		// private String ip;// IP
		private int port;// 端号号
		private ServerSocketChannel server = null;
		public boolean stop = false;

		public GeneralServer(int port) {
			super();
			this.port = port;
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
									final String req=pg.toString();
									
									Display display=Display.getDefault();
									display.asyncExec(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											txt_serveMonitor.append(req);
										}
									});
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
									// 同时关闭与客户端的连接
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
					//log.error(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//log.error(e.getMessage());
				}
			}

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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			System.out.println("打印服务器接收到的请求数据体：" + str);
			content = str.split(Sys_Constant.DELIMITER);// 初始化参数数组
			switch (pg.getCmdID()) {
			case Sys_Constant.COUNTER_CALL: // 窗口叫号
				// 这里要实现发送LED数据
				/*
				 * .......
				 */
				content = null;// 清空
				// 初始响应包的参数
				cmdID = Sys_Constant.COUNTER_CALL_REPLY;
				break;
			case Sys_Constant.COUNTER_PAUSE:// 窗口暂停
				// 这里要实现发送LED数据
				OS_Util.send_LED("暂停服务", content[1]);
				// 初始响应包的参数
				cmdID = Sys_Constant.COUNTER_PAUSE_REPLY;
				content = null;
				break;
			case Sys_Constant.ON_OFF:// 关机/重启
				// 初始响应包的参数
				cmdID = Sys_Constant.ON_OFF_REPLY;
				// 开启一条关机线程去执行关机或生重启任务(并且在程序上实现延时执行)
				// new ON_OFF_Thread(Integer.parseInt(content[1]),
				// Sys_Constant.SYS_TIME_DELAY).start();
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
				break;
			}
			// 封装返回结果
			pack = new PG_Package(21, cmdID, 0, pg.getSerialNo(), 0);

			return pack;
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

	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("通用通信测试程序");
		setSize(668, 495);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
