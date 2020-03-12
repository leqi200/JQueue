package com.tonsincs.task;

import org.apache.log4j.Logger;

import com.tonsincs.main.Call_Queue;
import com.tonsincs.main.NCall_Queue;
import com.tonsincs.main.Queues;
import com.tonsincs.util.OS_Util;
import com.tonsincs.util.SayNumbers;

/**
 * @ProjectName:JQueue
 * @ClassName: YiYinReadThread
 * @Description: TODO(这个是语音播报读取线程，用于读取排队呼叫队列)
 * @author 萧达光
 * @date 2014-5-29 下午03:43:08
 * 
 * @version V1.0
 */
public class YiYinReadThread extends Thread {
	private static Logger log = Logger.getLogger(YiYinReadThread.class);
	private boolean runing = true;
	private SayNumbers say = null;

	public YiYinReadThread() {
		say = new SayNumbers();
	}

	@Override
	public void run() {
		log.info("语音读取线程初始化成功！等待语音播放");
		System.out.println("语音读取线程初始化成功！等待语音播放");
		while (runing) {
			// 获取排队列呼叫号码
			Call_Queue call = Call_Queue.getInstance();
			Queues q1 = call.consumeQueue();
			System.out.println("播放：" + q1);
			if (q1 != null) {
				if (!say.sayingTheNumber) {
					say.say(q1.getCallNumber(), q1.getCounterNo());
					String msg = "请" + q1.getCallNumber() + "到"
							+ q1.getCounterNo() + "号窗口";
					OS_Util.send_LED(q1.getCounterNo() + "", msg);
					try {
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				log.error("", e);
			}
		}
	}
}
