package com.tonsincs.main;

/**
 * @ClassName: Call_Producter
 * @Description: TODO(叫号生产线程)
 * @author 萧达光
 * @date 2014-5-25 下午10:57:21
 * 
 * @version V1.0
 */
public class Call_Producter implements Runnable {
	private Queues queues=null;
	

	public Call_Producter(Queues queues) {
		this.queues = queues;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Call_Queue.getInstance().insertQueue(queues);
		}

	}

}
