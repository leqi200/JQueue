package com.tonsincs.main;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tonsincs.entity.PG_Package;
import com.tonsincs.util.OS_Util;
import com.tonsincs.util.SayNumbers;

/**
 * @ProjectName:JQueue
 * @ClassName: TestsCall
 * @Description: TODO(用于模拟测试叫号)
 * @author 萧达光
 * @date 2014-5-28 下午01:37:00
 * 
 * @version V1.0
 */
public class TestsCall {

	public static void main(String[] args) throws InterruptedException {
		startProductThread();
		startConsumThread();
		// PG_Package pack = new PG_Package(17, 0x00000201, 0, 0, 0);
		// System.out.println(pack.getBuf());
	}

	/**
	 * 开启生产者线程
	 */
	public static void startProductThread() {
		System.out.println("--生产者线程执行开始--");
		int pThreadSize = 10;
		// ExecutorService pool = Executors.newFixedThreadPool(pThreadSize);

		for (int i = 0; i < pThreadSize; i++) {
			// Call_Producter productThread = new Call_Producter(new
			// Queues("A00"+(i+1), (i+1), "A","综合业务"));
			// Thread thread = new Thread(productThread);
			// pool.execute(thread);
			Call_Queue.getInstance().insertQueue(
					new Queues("A00" + (i + 1), (i + 1), "A", "综合业务"));
		}
		System.out.println("--生产者线程执行结束--");
	}

	/**
	 * 开启消费者线程
	 * 
	 * @throws InterruptedException
	 */
	public static void startConsumThread() throws InterruptedException {
		System.out.println("--消费者线程执行开始--");
		int pThreadSize = 10;
		// ExecutorService pool = Executors.newFixedThreadPool(pThreadSize);
		// for (int i = 0; i < pThreadSize; i++) {
		// Queues q = Call_Queue.getInstance().consumeQueue();
		SayNumbers say = new SayNumbers();
		// System.out.println(q.toString());
		// // while (true) {
		// // say.say(q.getCallNumber(), q.getCounterNo());
		// // if (i) {
		// //
		// // }
		// // }
		//			
		// }
		// for (int i = 0; i < 10; i++) {
		// Queues q1 = Call_Queue.getInstance().consumeQueue();
		// System.out.println(q1);
		while (true) {
			Queues q1 = Call_Queue.getInstance().consumeQueue();
			System.out.println(q1);
			if (!say.sayingTheNumber) {
				say.say(q1.getCallNumber(), q1.getCounterNo());
				Thread.sleep(8000);
			}
		}
		// }
		//System.out.println("--消费者线程执行结束--");
	}
}
