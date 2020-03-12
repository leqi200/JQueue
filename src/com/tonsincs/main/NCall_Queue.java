package com.tonsincs.main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @ProjectName:JQueue
 * @ClassName: NCall_Queue
 * @Description: TODO(呼叫队列，用于存放呼叫排队号码),，内部实现队列的方式进行改进，同时也BlockingQueue JDK
 *               中的接口实现队列
 * @author 萧达光
 * @date 2015-05-18 下午16:09:17
 * 
 * @version V1.0
 */
public class NCall_Queue {
	/**
	 * @Fields MAXIMUM : TODO(最大库存存量)
	 */
	public static final int MAXIMUM = 100;

	/**
	 * @Fields CURRENTIMUM : TODO(当前库存容量)
	 */
	public static int CURRENTIMUM = 0;

	/**
	 * @Fields queues : TODO(创建一个队列数组用于存放队列信息)
	 */
	private BlockingQueue<Queues> QUEUES;

	/**
	 * @Fields INSTANCE : TODO(库存管理实例)
	 */
	private static NCall_Queue INSTANCE;

	private NCall_Queue() {
		QUEUES = new ArrayBlockingQueue<Queues>(MAXIMUM);
	};

	/**
	 * @Title: getInstance
	 * @Description: TODO(获取实唯一实例)
	 * @param @return
	 * @return NCall_Queue 返回类型
	 */
	public static NCall_Queue getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new NCall_Queue();
			return INSTANCE;
		}
		return INSTANCE;
	}

	/**
	 * @Title: insertQueue
	 * @Description: TODO(向排队列插入数据)
	 * @param queues
	 * @return void 返回类型
	 */
	public void insertQueue(Queues queues) {
		// 把排队信息存放到队列中
		QUEUES.add(queues);
	}

	/**
	 * @Title: consumeQueue
	 * @Description: TODO(从队列中取出叫号信息)
	 * @return void 返回类型
	 */
	public Queues consumeQueue() {
		try {
			return QUEUES.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
