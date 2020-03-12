package com.tonsincs.interfaces;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

/**
* @ProjectName:JQueue
* @ClassName: JQ_CLibrary
* @Description: TODO(与系统本地DLL库进行通讯，并且通过接口影射与DLL提供的接口进行交换)
* @author 萧达光
* @date 2014-5-28 下午01:37:45
* 
* @version V1.0 
*/
public interface JQ_CLibrary extends StdCallLibrary {
	JQ_CLibrary INSTANCE=(JQ_CLibrary)Native.loadLibrary("DllDemo",JQ_CLibrary.class);
	
	/**
	* @Title: closeCom
	* @Description: TODO(关闭与LED屏[N13A控制卡]通讯串口)
	* @param  
	* @return void 返回类型
	* @throws
	*/
	public void closeCom();	

	/**
	* @Title: sendCom32
	* @Description: TODO(发送信息到LED屏[N13A控制卡]通讯)
	* @param @param addr屏号
	* @param @param Line显示行
	* @param @param msg信息
	* @param @return 
	* @return int    返回类型
	* @throws
	*/
	public int sendCom32(int addr, int Line, String msg);

	/**
	* @Title: openCom
	* @Description: TODO(打开与LED屏[N13A控制卡 波特率为19200]通讯串口)
	* @param @param com_number COM口端口号
	* @param @param rate波特率(根据实际设置)
	* @param @return 
	* @return int    返回类型
	* @throws
	*/
	public int openCom(byte com_number, byte rate);
}
