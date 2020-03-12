package com.tonsincs.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/**
* @ProjectName:JQueue
* @ClassName: TonsincsSerialPortsPrint
* @Description: TODO(Tonsincs 串口通讯工具类:这个工具类主要是操作Tonsincs串口打印机)
* @author 萧达光
* @date 2014-5-28 下午01:33:33
* 
* @version V1.0 
*/
public class TonsincsSerialPortsPrint {
	static Enumeration portList;
	static CommPortIdentifier portId;
	static SerialPort serialPort;
	static OutputStream outputStream;
	static int PAGECOUNT = 1;
	private String serialName = "Print";
	private int timer = 3000;
	private String com;
	private static Map<String, String> map = new HashMap<String, String>();
	static {
		// 初始中英文转换字符集合
		map.put("，", ",");
		map.put("。", ".");
		map.put("〈", "<");
		map.put("〉", ">");
		map.put("‖", "|");
		map.put("《", "<");
		map.put("》", ">");
		map.put("〔", "(");
		map.put("〕", ")");
		map.put("﹖", "?");
		map.put("？", "?");
		map.put("“", "\"");
		map.put("”", "\"");
		map.put("：", ":");
		map.put("、", ",");
		map.put("（", "(");
		map.put("）", ")");
		map.put("【", "[");
		map.put("】", "]");
		map.put("—", "-");
		map.put("～", "~");
		map.put("！", "!");
		map.put("‵", "'");
		map.put("①", "1");
		map.put("②", "2");
		map.put("③", "3");
		map.put("④", "4");
		map.put("⑤", "5");
		map.put("⑥", "6");
		map.put("⑦", "7");
		map.put("⑧", "8");
		map.put("⑨", "9");
	}

	//初始始方法
	public TonsincsSerialPortsPrint(String com,int baudRate) {
		try {
			portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				portId = (CommPortIdentifier) portList.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (portId.getName().equals(com)) {
						// if (portId.getName().equals("/dev/term/a")) {
						try {
							serialPort = (SerialPort) portId.open(serialName,
									timer);
							
						} catch (PortInUseException e) {
						}
						try {
							outputStream = serialPort.getOutputStream();
						} catch (IOException e) {
							
						}
						try {
							serialPort.setSerialPortParams(baudRate,
									SerialPort.DATABITS_8,
									SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
						} catch (UnsupportedCommOperationException e) {
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private byte reciveData() {
		// TODO Auto-generated method stub

		return (byte) 128;
	}

	/**
	* @Title: close
	* @Description: TODO(关闭串口)
	* @param  
	* @return void    返回类型
	*/
	public void close(){
		if (serialPort!=null) {
			serialPort.close();
		}
	}
	/**
	 * @Title: tonsinsPrnterCutPaper
	 * @Description: TODO(控制打印机切纸)
	 * @param
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void tonsinsPrnterCutPaper() throws IOException {
		byte[] sbuff = new byte[1];
		sbuff[0] = 0x1d;
		sendData(sbuff, 1);
		sbuff[0] = 0x56;
		sendData(sbuff, 1);
		sbuff[0] = 0x66;
		sendData(sbuff, 1);
		sbuff[0] = 0x7f;
		sendData(sbuff, 1);
	}

	/**
	 * @Title: tonsinsPrnterBackPaper
	 * @Description: TODO(实现退纸功能函数)
	 * @param
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void tonsinsPrnterBackPaper() throws IOException {
		byte[] sbuff = new byte[3];
		sbuff[0] = 0x1b;
		sbuff[1] = 0x6a;
		sbuff[2] = 0x64;
		// 发送指令
		sendData(sbuff, sbuff.length);
	}

	/**
	 * @Title: tonsinsPrnterLF
	 * @Description: TODO(控制打印走纸一行)
	 * @param
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void tonsinsPrnterLF() throws IOException {
		byte[] sbuff = new byte[1];
		sbuff[0] = 0x0a;
		sendData(sbuff, sbuff.length);
	}

	public void SettonsinsPrnterNd() throws IOException {
		byte[] sbuff = new byte[1];
		sbuff[0] = 0x12;
		sendData(sbuff, 1);
		sbuff[0] = 0x7e;
		sendData(sbuff, 1);
		sbuff[0] = 0x03;
		sendData(sbuff, 1);
	}

	/**
	 * @Title: tonsinsPrnterSetFont
	 * @Description: TODO(设置字体大小)
	 * @param @param wf
	 * @param @param hf
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void tonsinsPrnterSetFont(byte wf, byte hf) throws IOException {
		byte[] sbuff = new byte[1];
		sbuff[0] = 0x1d;
		sendData(sbuff, 1);
		sbuff[0] = 0x21;
		sendData(sbuff, 1);
		sbuff[0] = (byte) ((wf << 4) | hf);
		sendData(sbuff, 1);
	}

	/**
	 * @Title: tonsincsPrinterPrintAbsoluteFix
	 * @Description: TODO(使用绝对定位来打印内容)
	 * @param @param contex
	 * @param @param x
	 * @param @param y
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void tonsincsPrinterPrintAbsoluteFix(String contex, int x, int y)
			throws IOException {
		byte[] sbuff = new byte[1];
		byte[] sendtat1 = contex.getBytes("GBK");

		int[] sendtat2 = new int[sendtat1.length];
		// 将字节数组存放到int 数组中保存
		for (int i = 0; i < sendtat1.length; i++) {
			sendtat2[i] = sendtat1[i] & 0xff;
		}
		// 再转换成字节数组
		for (int i = 0; i < sendtat2.length; i++) {
			byte[] temp = intToByteArray(sendtat2[i]);
			sendData(temp, 1);
		}

		sbuff[0] = 0x1b;
		sendData(sbuff, 1);

		sbuff[0] = 0x24;
		sendData(sbuff, 1);
		// (yh*256+yl)*0.125mm
		sbuff[0] = (byte) ((y * 256 + x) * 0.125);
		sendData(sbuff, 1);
		// sbuff[0] = (byte)x;
		// sendData(sbuff, 1);
		//
		// sbuff[0] = (byte)y;
		// sendData(sbuff, 1);
	}

	/**
	 * @Title: strFilter
	 * @Description: TODO(字符串的过滤器)
	 * @param @param content
	 * @param @return
	 * @return String 返回类型
	 */
	public String strreplace(String content) {
		int length = content.length();
		for (int i = 0; i < length; i++) {
			String charat = content.substring(i, i + 1);
			if (map.containsKey(charat)) {
				content = content.replace(charat, (String) map.get(charat));
			}
		}

		return content;
	}

	/**
	 * @Title: tonsinsPrnterPrintLine
	 * @Description: TODO(实现打印并且换行)
	 * @param @param contex
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void tonsinsPrnterPrintLine(String contex) throws IOException {
		byte[] sbuff = new byte[1];
		contex = strreplace(contex);// 中英字符转换处理
		byte[] sendtat1 = contex.getBytes("GBK");

		int[] sendtat2 = new int[sendtat1.length];
		// 将字节数组存放到int 数组中保存
		for (int i = 0; i < sendtat1.length; i++) {
			sendtat2[i] = sendtat1[i] & 0xff;
		}
		// 再转换成字节数组
		for (int i = 0; i < sendtat2.length; i++) {
			byte[] temp = intToByteArray(sendtat2[i]);
			sendData(temp, 1);
		}

		sbuff[0] = 0x0a;
		sendData(sbuff, 1);

	}

	/**
	 * int到byte[]
	 * 
	 * @param i
	 * @return
	 */
	public byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	/**
	 * @Title: sendData
	 * @Description: TODO(用于发送字节数据函数)
	 * @param @param sbuff
	 * @param @param bufflen
	 * @return boolean 返回类型
	 * @throws IOException
	 */
	public void sendData(byte[] sbuff, int bufflen) throws IOException {
		if (outputStream != null) {
			outputStream.write(sbuff);
		}
	}

}
