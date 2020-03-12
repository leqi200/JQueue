package com.tonsincs.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import org.apache.log4j.Logger;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.entity.PrintSuper;
import com.tonsincs.main.Call_Queue;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.main.NCall_Queue;
import com.tonsincs.main.Queues;
import com.tonsincs.net.JQ_ClientSocket;
import com.tonsincs.net.NClient;

/**
 * @ProjectName:JQueue
 * @ClassName: OS_Util
 * @Description: TODO(操作排队系统一些工具函数)
 * @author 萧达光
 * @date 2014-5-28 下午01:29:45
 * 
 * @version V1.0
 */
public class OS_Util {

	private static Logger log = Logger.getLogger(OS_Util.class);

	/**
	 * 这个叫号方法使用的是新的消息队列来存放叫号信息
	 * 
	 * @param counterNo窗口号
	 * @param serviceNo业务编号
	 * @param queueNumber排队号码
	 * @return如果呼叫成功返回true,否则false
	 */
	public static boolean counter_Call1(String counterNo, String serviceNo,
			String queueNumber) {
		NCall_Queue.getInstance().insertQueue(
				new Queues(queueNumber, Integer.parseInt(counterNo), serviceNo,
						"", "call"));
		return true;
	}

	/**
	 * @Title: counter_Call
	 * @Description: TODO(实现叫号功能函数)
	 * @param @param counterNo窗口号
	 * @param @param serviceNo业务编号
	 * @param @param queueNumber排队号码
	 * @param @return 如果呼叫成功返回true,否则false
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean counter_Call(String counterNo, String serviceNo,
			String queueNumber) {
		// SayNumbers say = new SayNumbers();
		// say.say(queueNumber, Integer.parseInt(counterNo));
		// String msg="请"+queueNumber+"到"+counterNo+"窗口";
		// send_LED(counterNo, msg);
		Call_Queue.getInstance().insertQueue(
				new Queues(queueNumber, Integer.parseInt(counterNo), serviceNo,
						""));
		return true;
	}

	/**
	 * @Title: send_LED
	 * @Description: TODO(发送数据到LED屏幕根据目前的情况，这个API只统一使用一个COM来发送数据，波特率也是使用默认的)
	 * @param @param counterNo 窗口号
	 * @param @param content 显示内容
	 * @param @return
	 * @return boolean 返回类型
	 */
	public static boolean send_LED(String counterNo, String content) {
		// SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
		// SerialPort.PARITY_NONE
		// 创建一个窗口屏LED消息发送对象
		// SendLEDMessage send = new SendLEDMessage(JQ_Main.OS_CONTEXT
		// .get("LED_COM"), Integer.parseInt(JQ_Main.OS_CONTEXT
		// .get("LED_BAUD_RATE")), SerialPort.DATABITS_8,
		// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

		try {
			int conAddress = Integer.parseInt(JQ_Main.OS_CONTEXT.get("COUNTER_"
					+ counterNo));
			// 创建一个窗口屏发送对象
			SendLEDMessage sled = new SendLEDMessage();
			byte[] content_data = sled.getSendLEDData_D(
					content.substring(0, content.length() - 1),
					Sys_Constant.LED_A_AREA, conAddress);
			// 创建一个综合屏发送对象
			SendLED_0509Message sled0509 = new SendLED_0509Message();
			byte[] sned_0509 = sled0509.getsendleddataS(content,
					Sys_Constant.LED_SEND_0509_BROADCAST_ADDRESS);
			// 获取发送串口数据对象
			CommonSendLEDMsg comm = CommonSendLEDMsg.getInstance();

			comm.openSerial(JQ_Main.OS_CONTEXT.get("COMMON_COM"));
			comm.sendData(content_data);
			comm.sendData(sned_0509);
			// 关闭串口
			CommonSendLEDMsg.getInstance().closeSerial();
		} catch (PortInUseException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} catch (UnsupportedCommOperationException e) {
			log.error("", e);
		} catch (InterruptedException e) {
			log.error("", e);
		}
		// 发送数据到LED屏显示
		// send.sendleddataD(content, Sys_Constant.LED_A_AREA,
		// Integer.parseInt(counterNo));
		return true;
	}

	/**
	 * @Title: counterNew_Pause
	 * @Description: TODO(窗口暂停叫号函数)
	 * @param @param counterNo窗口号
	 * @param @return 如果成功返回true,否则false
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean counterNew_Pause(String counterNo) {
		NCall_Queue.getInstance().insertQueue(
				new Queues("", Integer.parseInt(JQ_Main.OS_CONTEXT
						.get("COUNTER_" + counterNo)), "", "", "pause"));
		return true;
	}

	/**
	 * @Title: counter_Pause
	 * @Description: TODO(窗口暂停叫号函数)
	 * @param @param counterNo窗口号
	 * @param @return 如果成功返回true,否则false
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean counter_Pause(String counterNo) {
		// 创建一个LED消息发送对象
		// SendLEDMessage send = new SendLEDMessage(JQ_Main.OS_CONTEXT
		// .get("LED_COM"), Integer.parseInt(JQ_Main.OS_CONTEXT
		// .get("LED_BAUD_RATE")), SerialPort.DATABITS_8,
		// SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

		// 发送数据到LED屏显示
		try {
			int conAddress = Integer.parseInt(JQ_Main.OS_CONTEXT.get("COUNTER_"
					+ counterNo));
			// 创建一个窗口屏发送对象
			SendLEDMessage sled = new SendLEDMessage();
			byte[] content_data = sled.getSendLEDData_D(
					JQ_Main.OS_CONTEXT.get("PAUSE_SERVICE_CUE"),
					Sys_Constant.LED_A_AREA, conAddress);
			// 获取发送串口数据对象
			CommonSendLEDMsg comm = CommonSendLEDMsg.getInstance();
			comm.openSerial(JQ_Main.OS_CONTEXT.get("COMMON_COM"));
			comm.sendData(content_data);
			// 关闭串口
			CommonSendLEDMsg.getInstance().closeSerial();
		} catch (PortInUseException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} catch (UnsupportedCommOperationException e) {
			log.error("", e);
		} catch (InterruptedException e) {
			log.error("", e);
		}

		return true;

	}

	/**
	 * @Title: on_off
	 * @Description: TODO(操作排队机系统，关机、还是重启)
	 * @param @param key （1:关机、2：重启）
	 * @param @return
	 * @return boolean 返回类型
	 * @throws
	 */
	public static void on_off(int key) {
		try {
			switch (key) {
			case 1:
				// 关机
				Runtime.getRuntime().exec("shutdown -S");
				break;
			case 2:
				// 重启
				Runtime.getRuntime().exec("shutdown -R");
				break;
			default:
				break;
			}
		} catch (IOException e) {
			log.error("", e);
		}
	}

	/**
	 * @Title: commsend_PG_System
	 * @Description: TODO(公共函数，用于发送指令到管系统)
	 * @param @param pg
	 * @param @return
	 * @return PG_Package 返回类型
	 */
	public static PG_Package commsend_PG_System(PG_Package pg, String ip,
			int port) {
		PG_Package pack = null;
		try {
			// nClient = new NClient(ip,port);
			// pack= nClient.sendMsg(pg);
			JQ_ClientSocket jc = new JQ_ClientSocket(ip, port,
					Sys_Constant.SOCKET_OUT_TIME);
			pack = jc.sendMsg(pg);
		} catch (NumberFormatException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}

		return pack;
	}

	/**
	 * @Title: printTicketNo
	 * @Description: TODO(打印票号函数)
	 * @param @param printcontent 传入要打印的内容(注意、打印内容必需满足移动规定的数据格式)
	 * @return void 返回类型
	 */
	public static void printTicketNo(String printcontent) {
		// 获取数据
		String[] data = printcontent.split(Sys_Constant.DELIMITER);
		int style = 1;
		if (!data[5].equals("")) {
			if (!data[10].equals("")) {
				style = Sys_Constant.ALL_PRINT;
			} else {
				style = Sys_Constant.STOCK_PRINT;
			}
		} else {
			style = Sys_Constant.NOTICKET_PRINT;
		}
		// 创建打印样式
		PrintSuper ps = PrintFactory.createTicket(style, printcontent);

		ps.print();// 调用打印方法
	}

	/**
	 * @Title: updateOsContext
	 * @Description: TODO(用于更新系统的上下文信息,该方法是同步的)
	 * @param @param key 键
	 * @param @param value 值
	 * @return void 返回类型
	 * @throws
	 */
	public static synchronized void updateOsContext(String[] key, String[] value) {
		/*
		 * 先存入到一个临时集合中,并且同时修改更新系统的上下文信息， 然后再更新系统的上下文信息、最后还要同步到数据库中保存供程序下次调用
		 */
		for (int i = 0; i < key.length; i++) {
			// JQ_Main.RUN_TEMPORARY.put(key[i], value[i]); //
			// 存放在临时集合中的目的是方便数据库的数据更新
			JQ_Main.OS_CONTEXT.put(key[i], value[i]);
		}

		// 把需要同步的数据更新到数据库
		SQLHelper sqlHelper = new SQLHelper();
		sqlHelper.batchUpdate(key, value);
	}

	public static void main(String[] args) {
		// String str =
		// "AAGZ00001~广州分公司~中信营业厅~A~综合业务~X02~5~受理窗口:1、2、3~2014-03-19 16:20:30~你可以登录门户网站(http://www.gdchinamobile.com/gx/)的优惠资讯栏目，随时随地查询各类优惠活动的资讯。~主动营销内容:.............~取票手机:1392103233,请您凭号服务";
		// printTicketNo(str);
		counter_Call("2", "A", "A142");
		// counter_Pause("2");
	}
}
