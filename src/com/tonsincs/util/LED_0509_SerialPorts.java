package com.tonsincs.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import com.tonsincs.constant.Sys_Constant;

/**
 * @ProjectName:JQueue
 * @ClassName: LED_0509_SerialPorts
 * @Description: TODO(0507通讯协议封装类)
 * @author 萧达光
 * @date 2014-5-28 下午01:29:05
 * 
 * @version V1.0
 */
public class LED_0509_SerialPorts {
	static Enumeration portList;
	static CommPortIdentifier portId;
	static String messageString = "Hello, world!\n";
	static SerialPort serialPort;
	static OutputStream outputStream;
	static int PAGECOUNT = 1;
	private String portName; // 串口号
	private int baudRate; // 波特率
	// SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
	// SerialPort.PARITY_NONE
	private int databits;
	private int stopbits;
	private int parity;

	public LED_0509_SerialPorts(String portName) {
		this.portName = portName;
		this.baudRate = Sys_Constant.SYS_DEFALUT_BAUDRATE;
		this.databits = SerialPort.DATABITS_8;
		this.stopbits = SerialPort.STOPBITS_1;
		this.parity = SerialPort.PARITY_NONE;
	}

	/**
	 * @Title: sendleddataS
	 * @Description: TODO(发送LED显示数据)
	 * @param @param sendstr 发送显示内容
	 * @param @param address 屏地址
	 * @return void 返回类型
	 * @throws UnsupportedEncodingException
	 */
	public byte[] sendleddataS(String sendstr, int address)
			throws UnsupportedEncodingException {
		int i, j, crc, crc1, crc2;
		byte[] tlen = null;
		tlen = sendstr.getBytes(Sys_Constant.SYS_DEFAULT_ENCODED);
		byte[] sendbit = new byte[tlen.length + 12];
		sendbit[0] = (byte) 0xAA;
		sendbit[1] = (byte) 0xAA;
		sendbit[2] = (byte) 0xAA;
		sendbit[3] = (byte) 0x4E;
		sendbit[4] = (byte) (tlen.length + 8);
		sendbit[5] = (byte) 0x00;
		sendbit[6] = (byte) 0x53;
		sendbit[7] = (byte) 0x00;
		sendbit[8] = (byte) 0xFF;

		for (i = 0; i < tlen.length; i++) {
			sendbit[i + 9] = tlen[i];
		}

		crc = sendbit[4];
		for (j = 5; j < tlen.length + 9; j++) {
			crc = crc ^ sendbit[j];
		}
		crc1 = ((crc >> 4) & 0x0F);
		crc2 = (crc & 0x0F);
		sendbit[tlen.length + 9] = (byte) crc1;
		sendbit[tlen.length + 10] = (byte) crc2;
		sendbit[tlen.length + 11] = (byte) 0x1A;

		//datasendcom(sendbit);
		return sendbit;
	}

	/**
	 * @Title: datasendcom
	 * @Description: TODO(发送串口数据)
	 * @param @param buff 字节数组
	 * @param @param command 命令
	 * @return void 返回类型
	 */
	@Deprecated
	public void datasendcom(byte[] buff) {

		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(this.portName)) {
					// if (portId.getName().equals("/dev/term/a")) {
					try {
						serialPort = (SerialPort) portId.open("senddataled",
								3000);
					} catch (PortInUseException e) {
					}
					try {
						outputStream = serialPort.getOutputStream();
					} catch (IOException e) {
					}
					try {
						serialPort.setSerialPortParams(this.baudRate,
								this.databits, this.stopbits, this.parity);
					} catch (UnsupportedCommOperationException e) {
					}
					try {
						// 开始发送口数据
						outputStream.write(buff);
						outputStream.flush();
						try {
							Thread.sleep(180);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// tonsincsPrinterImage(image, 64);
						break;
					} catch (IOException e) {
					}
				}
			}
			// 关闭串口
			serialPort.close();
		}

	}

	public static void main(String[] args) throws Exception {
		LED_0509_SerialPorts led = new LED_0509_SerialPorts("COM3");
		led.sendleddataS("请A002到2号窗口", 0);
	}

}
