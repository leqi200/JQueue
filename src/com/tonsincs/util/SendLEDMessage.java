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
 * @ClassName: SendLEDMessage
 * @Description: TODO(这个类是的通讯协议根据LED 0807通信协议封装的)
 * @author 萧达光
 * @date 2014-5-28 下午01:31:11
 * 
 * @version V1.0
 */
public class SendLEDMessage {

	private String portName; // 串口号
	private int baudRate; // 波特率
	// SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
	// SerialPort.PARITY_NONE
	private int databits;
	private int stopbits;
	private int parity;

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:注意这个构造函数使用的是系统默认参数值来初始化与数据库里面的配置参数不一样
	 * </p>
	 * 
	 * @param portName
	 *            串口端口号
	 */
	public SendLEDMessage() { 
		this.baudRate = Sys_Constant.SYS_DEFALUT_BAUDRATE;
		this.databits = SerialPort.DATABITS_8;
		this.stopbits = SerialPort.STOPBITS_1;
		this.parity = SerialPort.PARITY_NONE;
	}

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param portName
	 *            使用串口的端口号
	 * @param baudRate
	 *            指定波特率
	 * @param databits
	 *            数据位
	 * @param stopbits
	 *            停止位
	 * @param parity
	 *            奇偶校验
	 */
	public SendLEDMessage(String portName, int baudRate, int databits,
			int stopbits, int parity) {
		super();
		this.portName = portName;
		this.baudRate = baudRate;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;
	}

	private static Enumeration portList;
	private static CommPortIdentifier portId;
	private static SerialPort serialPort;
	private static OutputStream outputStream;
	private static int PAGECOUNT = 1;
	private static int[] CRCTABLE = new int[256];
	// 静态块、执行一些初始操作
	static {
		CRCTABLE[0] = 0x0000;
		CRCTABLE[1] = 0x1189;
		CRCTABLE[2] = 0x2312;
		CRCTABLE[3] = 0x329b;
		CRCTABLE[4] = 0x4624;
		CRCTABLE[5] = 0x57ad;
		CRCTABLE[6] = 0x6536;
		CRCTABLE[7] = 0x74bf;
		CRCTABLE[8] = 0x8c48;
		CRCTABLE[9] = 0x9dc1;
		CRCTABLE[10] = 0xaf5a;
		CRCTABLE[11] = 0xbed3;
		CRCTABLE[12] = 0xca6c;
		CRCTABLE[13] = 0xdbe5;
		CRCTABLE[14] = 0xe97e;
		CRCTABLE[15] = 0xf8f7;
		CRCTABLE[16] = 0x1081;
		CRCTABLE[17] = 0x0108;
		CRCTABLE[18] = 0x3393;
		CRCTABLE[19] = 0x221a;
		CRCTABLE[20] = 0x56a5;
		CRCTABLE[21] = 0x472c;
		CRCTABLE[22] = 0x75b7;
		CRCTABLE[23] = 0x643e;
		CRCTABLE[24] = 0x9cc9;
		CRCTABLE[25] = 0x8d40;
		CRCTABLE[26] = 0xbfdb;
		CRCTABLE[27] = 0xae52;
		CRCTABLE[28] = 0xdaed;
		CRCTABLE[29] = 0xcb64;
		CRCTABLE[30] = 0xf9ff;
		CRCTABLE[31] = 0xe876;
		CRCTABLE[32] = 0x2102;
		CRCTABLE[33] = 0x308b;
		CRCTABLE[34] = 0x0210;
		CRCTABLE[35] = 0x1399;
		CRCTABLE[36] = 0x6726;
		CRCTABLE[37] = 0x76af;
		CRCTABLE[38] = 0x4434;
		CRCTABLE[39] = 0x55bd;
		CRCTABLE[40] = 0xad4a;
		CRCTABLE[41] = 0xbcc3;
		CRCTABLE[42] = 0x8e58;
		CRCTABLE[43] = 0x9fd1;
		CRCTABLE[44] = 0xeb6e;
		CRCTABLE[45] = 0xfae7;
		CRCTABLE[46] = 0xc87c;
		CRCTABLE[47] = 0xd9f5;
		CRCTABLE[48] = 0x3183;
		CRCTABLE[49] = 0x200a;
		CRCTABLE[50] = 0x1291;
		CRCTABLE[51] = 0x0318;
		CRCTABLE[52] = 0x77a7;
		CRCTABLE[53] = 0x662e;
		CRCTABLE[54] = 0x54b5;
		CRCTABLE[55] = 0x453c;
		CRCTABLE[56] = 0xbdcb;
		CRCTABLE[57] = 0xac42;
		CRCTABLE[58] = 0x9ed9;
		CRCTABLE[59] = 0x8f50;
		CRCTABLE[60] = 0xfbef;
		CRCTABLE[61] = 0xea66;
		CRCTABLE[62] = 0xd8fd;
		CRCTABLE[63] = 0xc974;
		CRCTABLE[64] = 0x4204;
		CRCTABLE[65] = 0x538d;
		CRCTABLE[66] = 0x6116;
		CRCTABLE[67] = 0x709f;
		CRCTABLE[68] = 0x0420;
		CRCTABLE[69] = 0x15a9;
		CRCTABLE[70] = 0x2732;
		CRCTABLE[71] = 0x36bb;
		CRCTABLE[72] = 0xce4c;
		CRCTABLE[73] = 0xdfc5;
		CRCTABLE[74] = 0xed5e;
		CRCTABLE[75] = 0xfcd7;
		CRCTABLE[76] = 0x8868;
		CRCTABLE[77] = 0x99e1;
		CRCTABLE[78] = 0xab7a;
		CRCTABLE[79] = 0xbaf3;
		CRCTABLE[80] = 0x5285;
		CRCTABLE[81] = 0x430c;
		CRCTABLE[82] = 0x7197;
		CRCTABLE[83] = 0x601e;
		CRCTABLE[84] = 0x14a1;
		CRCTABLE[85] = 0x0528;
		CRCTABLE[86] = 0x37b3;
		CRCTABLE[87] = 0x263a;
		CRCTABLE[88] = 0xdecd;
		CRCTABLE[89] = 0xcf44;
		CRCTABLE[90] = 0xfddf;
		CRCTABLE[91] = 0xec56;
		CRCTABLE[92] = 0x98e9;
		CRCTABLE[93] = 0x8960;
		CRCTABLE[94] = 0xbbfb;
		CRCTABLE[95] = 0xaa72;
		CRCTABLE[96] = 0x6306;
		CRCTABLE[97] = 0x728f;
		CRCTABLE[98] = 0x4014;
		CRCTABLE[99] = 0x519d;
		CRCTABLE[100] = 0x2522;
		CRCTABLE[101] = 0x34ab;
		CRCTABLE[102] = 0x0630;
		CRCTABLE[103] = 0x17b9;
		CRCTABLE[104] = 0xef4e;
		CRCTABLE[105] = 0xfec7;
		CRCTABLE[106] = 0xcc5c;
		CRCTABLE[107] = 0xddd5;
		CRCTABLE[108] = 0xa96a;
		CRCTABLE[109] = 0xb8e3;
		CRCTABLE[110] = 0x8a78;
		CRCTABLE[111] = 0x9bf1;
		CRCTABLE[112] = 0x7387;
		CRCTABLE[113] = 0x620e;
		CRCTABLE[114] = 0x5095;
		CRCTABLE[115] = 0x411c;
		CRCTABLE[116] = 0x35a3;
		CRCTABLE[117] = 0x242a;
		CRCTABLE[118] = 0x16b1;
		CRCTABLE[119] = 0x0738;
		CRCTABLE[120] = 0xffcf;
		CRCTABLE[121] = 0xee46;
		CRCTABLE[122] = 0xdcdd;
		CRCTABLE[123] = 0xcd54;
		CRCTABLE[124] = 0xb9eb;
		CRCTABLE[125] = 0xa862;
		CRCTABLE[126] = 0x9af9;
		CRCTABLE[127] = 0x8b70;
		CRCTABLE[128] = 0x8408;
		CRCTABLE[129] = 0x9581;
		CRCTABLE[130] = 0xa71a;
		CRCTABLE[131] = 0xb693;
		CRCTABLE[132] = 0xc22c;
		CRCTABLE[133] = 0xd3a5;
		CRCTABLE[134] = 0xe13e;
		CRCTABLE[135] = 0xf0b7;
		CRCTABLE[136] = 0x0840;
		CRCTABLE[137] = 0x19c9;
		CRCTABLE[138] = 0x2b52;
		CRCTABLE[139] = 0x3adb;
		CRCTABLE[140] = 0x4e64;
		CRCTABLE[141] = 0x5fed;
		CRCTABLE[142] = 0x6d76;
		CRCTABLE[143] = 0x7cff;
		CRCTABLE[144] = 0x9489;
		CRCTABLE[145] = 0x8500;
		CRCTABLE[146] = 0xb79b;
		CRCTABLE[147] = 0xa612;
		CRCTABLE[148] = 0xd2ad;
		CRCTABLE[149] = 0xc324;
		CRCTABLE[150] = 0xf1bf;
		CRCTABLE[151] = 0xe036;
		CRCTABLE[152] = 0x18c1;
		CRCTABLE[153] = 0x0948;
		CRCTABLE[154] = 0x3bd3;
		CRCTABLE[155] = 0x2a5a;
		CRCTABLE[156] = 0x5ee5;
		CRCTABLE[157] = 0x4f6c;
		CRCTABLE[158] = 0x7df7;
		CRCTABLE[159] = 0x6c7e;
		CRCTABLE[160] = 0xa50a;
		CRCTABLE[161] = 0xb483;
		CRCTABLE[162] = 0x8618;
		CRCTABLE[163] = 0x9791;
		CRCTABLE[164] = 0xe32e;
		CRCTABLE[165] = 0xf2a7;
		CRCTABLE[166] = 0xc03c;
		CRCTABLE[167] = 0xd1b5;
		CRCTABLE[168] = 0x2942;
		CRCTABLE[169] = 0x38cb;
		CRCTABLE[170] = 0x0a50;
		CRCTABLE[171] = 0x1bd9;
		CRCTABLE[172] = 0x6f66;
		CRCTABLE[173] = 0x7eef;
		CRCTABLE[174] = 0x4c74;
		CRCTABLE[175] = 0x5dfd;
		CRCTABLE[176] = 0xb58b;
		CRCTABLE[177] = 0xa402;
		CRCTABLE[178] = 0x9699;
		CRCTABLE[179] = 0x8710;
		CRCTABLE[180] = 0xf3af;
		CRCTABLE[181] = 0xe226;
		CRCTABLE[182] = 0xd0bd;
		CRCTABLE[183] = 0xc134;
		CRCTABLE[184] = 0x39c3;
		CRCTABLE[185] = 0x284a;
		CRCTABLE[186] = 0x1ad1;
		CRCTABLE[187] = 0x0b58;
		CRCTABLE[188] = 0x7fe7;
		CRCTABLE[189] = 0x6e6e;
		CRCTABLE[190] = 0x5cf5;
		CRCTABLE[191] = 0x4d7c;
		CRCTABLE[192] = 0xc60c;
		CRCTABLE[193] = 0xd785;
		CRCTABLE[194] = 0xe51e;
		CRCTABLE[195] = 0xf497;
		CRCTABLE[196] = 0x8028;
		CRCTABLE[197] = 0x91a1;
		CRCTABLE[198] = 0xa33a;
		CRCTABLE[199] = 0xb2b3;
		CRCTABLE[200] = 0x4a44;
		CRCTABLE[201] = 0x5bcd;
		CRCTABLE[202] = 0x6956;
		CRCTABLE[203] = 0x78df;
		CRCTABLE[204] = 0x0c60;
		CRCTABLE[205] = 0x1de9;
		CRCTABLE[206] = 0x2f72;
		CRCTABLE[207] = 0x3efb;
		CRCTABLE[208] = 0xd68d;
		CRCTABLE[209] = 0xc704;
		CRCTABLE[210] = 0xf59f;
		CRCTABLE[211] = 0xe416;
		CRCTABLE[212] = 0x90a9;
		CRCTABLE[213] = 0x8120;
		CRCTABLE[214] = 0xb3bb;
		CRCTABLE[215] = 0xa232;
		CRCTABLE[216] = 0x5ac5;
		CRCTABLE[217] = 0x4b4c;
		CRCTABLE[218] = 0x79d7;
		CRCTABLE[219] = 0x685e;
		CRCTABLE[220] = 0x1ce1;
		CRCTABLE[221] = 0x0d68;
		CRCTABLE[222] = 0x3ff3;
		CRCTABLE[223] = 0x2e7a;
		CRCTABLE[224] = 0xe70e;
		CRCTABLE[225] = 0xf687;
		CRCTABLE[226] = 0xc41c;
		CRCTABLE[227] = 0xd595;
		CRCTABLE[228] = 0xa12a;
		CRCTABLE[229] = 0xb0a3;
		CRCTABLE[230] = 0x8238;
		CRCTABLE[231] = 0x93b1;
		CRCTABLE[232] = 0x6b46;
		CRCTABLE[233] = 0x7acf;
		CRCTABLE[234] = 0x4854;
		CRCTABLE[235] = 0x59dd;
		CRCTABLE[236] = 0x2d62;
		CRCTABLE[237] = 0x3ceb;
		CRCTABLE[238] = 0x0e70;
		CRCTABLE[239] = 0x1ff9;
		CRCTABLE[240] = 0xf78f;
		CRCTABLE[241] = 0xe606;
		CRCTABLE[242] = 0xd49d;
		CRCTABLE[243] = 0xc514;
		CRCTABLE[244] = 0xb1ab;
		CRCTABLE[245] = 0xa022;
		CRCTABLE[246] = 0x92b9;
		CRCTABLE[247] = 0x8330;
		CRCTABLE[248] = 0x7bc7;
		CRCTABLE[249] = 0x6a4e;
		CRCTABLE[250] = 0x58d5;
		CRCTABLE[251] = 0x495c;
		CRCTABLE[252] = 0x3de3;
		CRCTABLE[253] = 0x2c6a;
		CRCTABLE[254] = 0x1ef1;
		CRCTABLE[255] = 0x0f78;
	}

	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param args
	 * @return void 返回类型
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// sendleddataD("请A013到6号窗口", 0x01, 0x02);
		// SendLEDMessage send=new SendLEDMessage("COM3");
		// send.sendleddataD("请A013到6号窗口", 0x01, 0x02);
		String res = "0012399409_03_39";
		String req = res.split("_")[1].equals("01") ? "200" : "201";// 01表示扣费成功，02表示扣费失败
		System.out.println(req);
	}

	/**
	 * @Title: createCRC
	 * @Description: TODO(CRC检验函数，目前以过时，不建使用)
	 * @param @param buff
	 * @param @return
	 * @return int 返回类型
	 */
	@Deprecated
	public static int createCRC(byte[] buff) {
		int crc = 0xffff;
		for (byte b : buff) {
			crc = (crc >> 8) ^ CRCTABLE[(crc ^ b) & 0xff];
		}

		return crc;
	}

	public static int createCRC(int[] buff, int len) {
		int crc = 0xffff;
		for (int i = 0; i < len; i++) {
			crc = (crc >> 8) ^ CRCTABLE[(crc ^ buff[i]) & 0xff];
		}
		return ~crc;
	}

	/**
	 * int到byte[]
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	/**
	* @Title: getSendLEDData_D
	* @Description: TODO(获取要发送的数据字节数组)
	* @param @param sendstr 发送内容
	* @param @param area 显示区域
	* @param @param address 目标LED屏地址
	* @param @return 
	* @return byte[]    返回类型
	*/
	public byte[] getSendLEDData_D(String sendstr, int area, int address) {
		if (sendstr == null) {
			return "".getBytes();
		}
		// 定义一些基本参数
		int[] buff;
		// char[] str_ascii;
		byte[] str_ascii1 = null;
		int crc = 0, crc1, crc2;
		// 把字符串转换成字符数组
		try {
			str_ascii1 = sendstr.getBytes("GBK");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buff = new int[str_ascii1.length + 7];
		buff[0] = (byte) address;
		buff[1] = 0xe9;
		// buff[1] = new Byte("0xe9");
		buff[2] = (byte) 'D';
		buff[3] = (byte) area;
		buff[4] = (byte) 0x09;
		System.out.println("D=" + ((byte) 'D'));
		for (int i = 0; i < str_ascii1.length; i++) {
			// System.out.println(" " + str_ascii[i] + " " + (int)
			// str_ascii[i]+"byte"+(byte) str_ascii[i]);
			buff[i + 5] = str_ascii1[i] & 0xff;
		}
		crc = createCRC(buff, buff.length - 2);
		// 插入校验码
		crc1 = ((crc >> 8) & 0x00FF);
		crc2 = (crc & 0x00FF);
		buff[buff.length - 1] = crc1;
		buff[buff.length - 2] = crc2;

		// 转换
		// 定义一个长度为250的字节数组(也就是说目前只能发250个字节数据)
		int[] datasendbuff = new int[250];
		byte[] datasendbuff2 = new byte[1024];
		int k = 0;
		datasendbuff[k] = (byte) 0x7e;
		k++;
		boolean issend = false;
		for (int i = 0; i < buff.length; i++) {
			issend = false;
			if (buff[i] < (byte) 0x20) {
				issend = true;
				datasendbuff[k] = (byte) 0x7d;
				k++;
				datasendbuff[k] = buff[i] + (byte) 0x20;
				k++;
			}
			if (buff[i] == (byte) 0x7e) {
				issend = true;
				datasendbuff[k] = (byte) 0x7d;
				k++;
				datasendbuff[k] = (byte) 0x5e;
				k++;
			}
			if (buff[i] == (byte) 0x7d) {
				issend = true;
				datasendbuff[k] = (byte) 0x7d;
				k++;
				datasendbuff[k] = (byte) 0x5d;
				k++;
			}
			if (!issend) {
				datasendbuff[k] = buff[i];
				k++;
			}
		}

		// 结束标记
		datasendbuff[k] = (byte) 0x7e;
		// 把k置零
		k = 0;
		// 转换成字节数组
		for (int i = 0; i < datasendbuff.length; i++) {
			// 把每一个int 数据取出来放到byte[]数组中
			byte[] temp = intToByteArray(datasendbuff[i]);
			datasendbuff2[k] = temp[3];
			k++;
		}
		return datasendbuff2;
	}

	/**
	 * @Title: sendleddataD
	 * @Description: TODO(发送LED显示数据)
	 * @param @param sendstr 发送内容
	 * @param @param area 区域
	 * @param @param address LED屏地址
	 * @return void 返回类型
	 */
	public void sendleddataD(String sendstr, int area, int address) {
		if (sendstr == null) {
			return;
		}
		// 定义一些基本参数
		int[] buff;
		// char[] str_ascii;
		byte[] str_ascii1 = null;
		int crc = 0, crc1, crc2;
		// 把字符串转换成字符数组
		try {
			str_ascii1 = sendstr.getBytes("GBK");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buff = new int[str_ascii1.length + 7];
		buff[0] = (byte) address;
		buff[1] = 0xe9;
		// buff[1] = new Byte("0xe9");
		buff[2] = (byte) 'D';
		buff[3] = (byte) area;
		buff[4] = (byte) 0x09;
		System.out.println("D=" + ((byte) 'D'));
		for (int i = 0; i < str_ascii1.length; i++) {
			// System.out.println(" " + str_ascii[i] + " " + (int)
			// str_ascii[i]+"byte"+(byte) str_ascii[i]);
			buff[i + 5] = str_ascii1[i] & 0xff;
		}
		crc = createCRC(buff, buff.length - 2);
		// 插入校验码
		crc1 = ((crc >> 8) & 0x00FF);
		crc2 = (crc & 0x00FF);
		buff[buff.length - 1] = crc1;
		buff[buff.length - 2] = crc2;

		// 调用发送串口数组函数
		datasendcom(buff, (byte) 'D');
	}

	/**
	 * @Title: datasendcom
	 * @Description: TODO(发送串口数据)
	 * @param @param buff 字节数组
	 * @param @param command 命令
	 * @return void 返回类型
	 */
	// public static void datasendcom(byte[] buff, byte command) {
	// // 定义一个长度为250的字节数组(也就是说目前只能发250个字节数据)
	// byte[] datasendbuff = new byte[250];
	// int k = 0;
	// datasendbuff[k] = 0x7e;
	// k++;
	// boolean issend = false;
	// for (int i = 0; i < buff.length; i++) {
	// if (buff[i] < 0x20) {
	// issend = true;
	// datasendbuff[k] = 0x7d;
	// k++;
	// datasendbuff[k] = (byte) (buff[i] + 0x20);
	// k++;
	// }
	// if (buff[i] == 0x7e) {
	// issend = true;
	// datasendbuff[k] = 0x7d;
	// k++;
	// datasendbuff[k] = 0x5e;
	// k++;
	// }
	// if (buff[i] == 0x7d) {
	// issend = true;
	// datasendbuff[k] = 0x7d;
	// k++;
	// datasendbuff[k] = 0x5d;
	// k++;
	// }
	// if (!issend) {
	// // 复制到新的数组中
	// System.arraycopy(buff, 0, datasendbuff, 1,
	// datasendbuff.length - 1);
	// k++;
	// }
	// }
	//
	// // 结束标记
	// datasendbuff[k] = 0x7e;
	//
	// portList = CommPortIdentifier.getPortIdentifiers();
	//
	// while (portList.hasMoreElements()) {
	// portId = (CommPortIdentifier) portList.nextElement();
	// if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	// if (portId.getName().equals("COM3")) {
	// // if (portId.getName().equals("/dev/term/a")) {
	// try {
	// serialPort = (SerialPort) portId.open("SimpleWriteApp",
	// 3000);
	// } catch (PortInUseException e) {
	// }
	// try {
	// outputStream = serialPort.getOutputStream();
	// } catch (IOException e) {
	// }
	// try {
	// serialPort.setSerialPortParams(19200,
	// SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
	// SerialPort.PARITY_NONE);
	// } catch (UnsupportedCommOperationException e) {
	// }
	// try {
	// // 开始发送口数据
	// outputStream.write(datasendbuff);
	// outputStream.flush();
	// try {
	// Thread.sleep(180);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// // tonsincsPrinterImage(image, 64);
	// break;
	// } catch (IOException e) {
	// }
	// }
	// }
	// }
	// serialPort.close();
	// }

	/**
	 * @Title: datasendcom
	 * @Description: TODO(发送串口数据)
	 * 注意目前这个API作费，不作使用
	 * @param @param buff 字节数组
	 * @param @param command 命令
	 * @return void 返回类型
	 */
	@Deprecated
	public void datasendcom(int[] buff, int command) {
		// 定义一个长度为250的字节数组(也就是说目前只能发250个字节数据)
		int[] datasendbuff = new int[250];
		byte[] datasendbuff2 = new byte[1024];
		int k = 0;
		datasendbuff[k] = (byte) 0x7e;
		k++;
		boolean issend = false;
		for (int i = 0; i < buff.length; i++) {
			issend = false;
			if (buff[i] < (byte) 0x20) {
				issend = true;
				datasendbuff[k] = (byte) 0x7d;
				k++;
				datasendbuff[k] = buff[i] + (byte) 0x20;
				k++;
			}
			if (buff[i] == (byte) 0x7e) {
				issend = true;
				datasendbuff[k] = (byte) 0x7d;
				k++;
				datasendbuff[k] = (byte) 0x5e;
				k++;
			}
			if (buff[i] == (byte) 0x7d) {
				issend = true;
				datasendbuff[k] = (byte) 0x7d;
				k++;
				datasendbuff[k] = (byte) 0x5d;
				k++;
			}
			if (!issend) {
				datasendbuff[k] = buff[i];
				k++;
			}
		}

		// 结束标记
		datasendbuff[k] = (byte) 0x7e;
		// 把k置零
		k = 0;
		// 转换成字节数组
		for (int i = 0; i < datasendbuff.length; i++) {
			// 把每一个int 数据取出来放到byte[]数组中
			byte[] temp = intToByteArray(datasendbuff[i]);
			// for (int j = 0; j < temp.length; j++) {
			// System.out.println(k);
			datasendbuff2[k] = temp[3];
			k++;
			// }
			// k++;
		}
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
						outputStream.write(datasendbuff2);
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

}
