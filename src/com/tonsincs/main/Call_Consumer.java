package com.tonsincs.main;

import com.tonsincs.util.OS_Util;

/**
* @ProjectName:JQueue
* @ClassName: Call_Consumer
* @Description: TODO(主要用于产生消费叫号队的线程类)
* @author 萧达光
* @date 2014-5-28 下午01:34:04
* 
* @version V1.0 
*/
public class Call_Consumer implements Runnable {

	@Override
	public void run() {
		while (true) {
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			Queues q = Call_Queue.getInstance().consumeQueue();
			OS_Util.counter_Call(q.getCounterNo() + "", q.getBizNo(), q
					.getCallNumber());

		}
	}

}
