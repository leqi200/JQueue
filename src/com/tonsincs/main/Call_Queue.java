package com.tonsincs.main;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;


/**
* @ProjectName:JQueue
* @ClassName: Call_Queue
* @Description: TODO(呼叫队列，用于存放呼叫排队号码)
* @author 萧达光
* @date 2014-5-28 下午01:35:17
* 
* @version V1.0 
*/
public class Call_Queue {
	/**
	* @Fields MAXIMUM : TODO(最大库存存量)
	*/
	public static final int MAXIMUM =10;
	
	/**
	* @Fields CURRENTIMUM : TODO(当前库存容量)
	*/
	public static int CURRENTIMUM =0;
	
	
	/**
	* @Fields queues : TODO(创建一个队列数组用于存放队列信息)
	*/
	private static Vector<Queues> QUEUES= new Vector<Queues>();
	
	/**
	* @Fields INSTANCE : TODO(库存管理实例)
	*/
	private static Call_Queue INSTANCE;
	
	private Call_Queue(){};
	
	/**
	* @Title: getInstance
	* @Description: TODO(获取实唯一实例)
	* @param @return 
	* @return Call_Queue    返回类型
	*/
	public static Call_Queue getInstance(){
		if( INSTANCE==null){
			INSTANCE=new Call_Queue();
			return INSTANCE;
		}
		return INSTANCE;
	}
	
	/**
	* @Title: insertQueue
	* @Description: TODO(向排队列插入数据)
	* @param @param queues 
	* @return void    返回类型
	*/
	public synchronized void insertQueue(Queues queues) {
		// while (CURRENTIMUM>=MAXIMUM) {
		// // try {
		// // //wait(); //等待
		// // } catch (InterruptedException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// //
		// }
		//把排队信息存放到队列中
		QUEUES.add(queues);
		//System.out.println("存放在排队队列中的信息"+queues);
		if (QUEUES.size()>0) {
			//唤醒线程
			INSTANCE.notify();
		}
	}

	/**
	* @Title: consumeQueue
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param  
	* @return void    返回类型
	*/
	public synchronized Queues consumeQueue(){
		while (QUEUES.size()==0) {
			try {
				INSTANCE.wait();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		Queues queue=QUEUES.get(0);//获取第一个
		//System.out.println("取得队列信息："+queue);
		QUEUES.remove(0);//删除取得的数据
		return queue;
	}
}
