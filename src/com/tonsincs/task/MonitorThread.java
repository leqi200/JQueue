package com.tonsincs.task;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.tonsincs.main.JQ_Main;
import com.tonsincs.main.NCall_Queue;
import com.tonsincs.main.Queues;

/**
 * 与排队控制系统通信线程
 * 
 * @author dellb
 *
 */
public class MonitorThread extends Thread {

	private static Logger log = Logger.getLogger(MonitorThread.class);
	private boolean runing = true;
	private Socket socket = null;
	private String ip;
	private int port;

	public MonitorThread(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run() {

		InetSocketAddress address = new InetSocketAddress(ip, port);
		// DataInputStream dis = null;
		DataOutputStream dos = null;
		NCall_Queue call_Queue = null;
		String call_Temple = JQ_Main.OS_CONTEXT.get("CALL_TEMPLET");
		String pause_Temple = JQ_Main.OS_CONTEXT.get("PAUSE_TEMPLET");
		String msg = null;
		try {
			call_Queue = NCall_Queue.getInstance();

			while (runing) {
				log.info("等待排队队列表消息...");
				//System.out.println("等待排队队列表消息...");
				// 发送通知信息 D234001A000100P
				Queues q = call_Queue.consumeQueue();

				socket = new Socket();
				socket.connect(address, 3000);
				// dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(new BufferedOutputStream(
						socket.getOutputStream()));

				if (q.getAction().equals("call")) {
					msg = String.format(call_Temple, q.getCounterNo(),
							q.getCallNumber(), 0);
				} else if (q.getAction().equals("pause")) {
					msg = String.format(pause_Temple, q.getCounterNo());
				} else {
					msg = "";
					log.info("找不到匹配的动作指令");
				}

				log.info("发送消息到排队控制系统:" + msg);
				//System.out.println("发送消息到排队控制系统:" + msg);

				
				if (msg!=null && !"".equals(msg.trim())) {
					dos.writeBytes(msg);
					dos.flush();
				}
				
				socket.shutdownOutput();

				dos.close();
				socket.close();
				socket = null;
				msg = null;
			}
		} catch (IOException e) {
			log.error("", e);
		}

	}

	 public static void main(String[] args) {
	 String call = "D234%1$03d%2$s%3$02dP";
	// String pause = "D234%1$03d0000000E";
	//
	 Queues q = new Queues("A0001", 1, "", "");
	 System.out.println(String.format(call, q.getCounterNo(),
	 q.getCallNumber(), 0));
	// Queues q1 = new Queues("A0001", 1, "", "");
	// System.out.println(String.format(pause, q1.getCounterNo()));
	//
	// // 产生排队号码程序
	// new Thread(new Runnable() {
	// private NCall_Queue nq = NCall_Queue.getInstance();
	//
	// @Override
	// public void run() {
	// while (true) {
	//
	// nq.insertQueue(new Queues("A0001", 2, "02", "AAS"));
	// try {
	// System.out.println("休眠10秒钟");
	// Thread.sleep(1000 * 10);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// }).start();
	//
	// // 启动消费线程
	// MonitorThread monitorThread = new MonitorThread("192.168.10.102", 18086);
	// monitorThread.start();
	}

	public void stopMonitorThread() {
		this.runing = false;
	}
}
