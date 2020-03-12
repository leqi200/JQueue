package com.tonsincs.task;

import com.tonsincs.util.OS_Util;

/**
* @ProjectName:JQueue
* @ClassName: ON_OFF_Thread
* @Description: TODO(开关机生重启线程)
* @author 萧达光
* @date 2014-5-28 下午01:26:55
* 
* @version V1.0 
*/
public class ON_OFF_Thread extends Thread {
	private  int cmd;
	private  long times;
	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param cmd 开机还是重启命令
	* @param times延时毫秒数
	*/
	public ON_OFF_Thread(int cmd,long times) {
		this.cmd = cmd;
		this.times=times;
	}

	@Override
	public void run() {
		try {
			
			Thread.sleep(times);	//延时多少秒后执行
			OS_Util.on_off(cmd);	//调用系统的关机指令			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
		
	}
}
