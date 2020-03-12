package com.tonsincs.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import com.tonsincs.constant.Sys_Constant;

/**
 * @ProjectName:JQueue
 * @ClassName: CommonSendLEDMsg
 * @Description: TODO(公共发送LED串口数据的类，注意目前这个类没有监听返回的数据，以后考虑增加)
 * @author 萧达光
 * @date 2014-5-30 上午10:30:59
 * 
 * @version V1.0
 */
public class CommonSendLEDMsg {

	private String portName; // 串口号
	private int baudRate; // 波特率
	// SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
	// SerialPort.PARITY_NONE
	private int databits;
	private int stopbits;
	private int parity;
	private static final String OMMON_NAME = "SENDDATALED";

	private static Enumeration portList;
	private static CommPortIdentifier portId;
	private static SerialPort serialPort;
	private static OutputStream outputStream;
	private static CommonSendLEDMsg INSTANCE=null;
	
	// this.baudRate = Sys_Constant.SYS_DEFALUT_BAUDRATE;
	// this.databits = SerialPort.DATABITS_8;
	// this.stopbits = SerialPort.STOPBITS_1;
	// this.parity = SerialPort.PARITY_NONE;

	/**
	* <p>Title: </p>
	* <p>Description: 创建一个默认公共发送串口数据对象</p>
	*/
	private CommonSendLEDMsg() {
		this.baudRate = Sys_Constant.SYS_DEFALUT_BAUDRATE;
		this.databits = SerialPort.DATABITS_8;
		this.stopbits = SerialPort.STOPBITS_1;
		this.parity = SerialPort.PARITY_NONE;
	}
	
	/**
	* @Title: getInstance
	* @Description: TODO(创建唯一实例)
	* @param @return 
	* @return CommonSendLEDMsg    返回类型
	*/
	public static CommonSendLEDMsg getInstance(){
		if(INSTANCE==null){
			return new CommonSendLEDMsg();
		}
		return INSTANCE;
	}

	/**
	* <p>Title: </p>
	* <p>Description: 创建公共发送串口数据对象</p>
	* @param baudRate 波特率
	* @param databits 数据位
	* @param stopbits 停止位
	* @param parity 奇偶校验
	*/
	// public CommonSendLEDMsg(int baudRate, int databits, int stopbits, int
	// parity) {
	// this.baudRate = baudRate;
	// this.databits = databits;
	// this.stopbits = stopbits;
	// this.parity = parity;
	// }

	/**
	* @Title: openSerial
	* @Description: TODO(打开串口函数)
	* @param @param portName 打开指定的串口
	* @param @throws PortInUseException
	* @param @throws IOException
	* @param @throws UnsupportedCommOperationException 
	* @return void    返回类型
	*/
	public void openSerial(String portName) throws PortInUseException,
			IOException, UnsupportedCommOperationException {
		// 获取外设设备列表
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(portName)) {
					// if (portId.getName().equals("/dev/term/a"))
					// {//Linux平台打开串判断串口
					// 打开串口
					serialPort = (SerialPort) portId.open(OMMON_NAME, 3000);
					// 配置串口通信参数
					serialPort.setSerialPortParams(this.baudRate,
							this.databits, this.stopbits, this.parity);
					// 获得输出流
					outputStream = serialPort.getOutputStream();
				}
			}

		}
	}

	/**
	 * @Title: closeSerial
	 * @Description: TODO(关闭串口通信端口并释放占用的资源)
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void closeSerial() throws IOException {
		if (outputStream != null) {
			outputStream.close();
		}
		if (serialPort != null) {
			// 关闭串口
			serialPort.close();
		}
	}

	/**
	 * @Title: sendData
	 * @Description: TODO(这个函数只接收根据东岘源LED屏的通信协议组合好的字节数组)
	 * @param @param data 根据不同协议组合好的字节数组
	 * @return void 返回类型
	 */
	public void sendData(byte[] data) throws IOException, InterruptedException {
		if (outputStream!=null) {
			// 开始发送口数据
			outputStream.write(data);
			outputStream.flush();
			Thread.sleep(180);
		}
	}

}
