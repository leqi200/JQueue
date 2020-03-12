package com.tonsincs.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/**
* @ProjectName:JQueue
* @ClassName: SerialPorts
* @Description: TODO(串口通讯模块)
* @author 萧达光
* @date 2014-5-28 下午01:31:39
* 
* @version V1.0 
*/
public class SerialPorts {
	static Enumeration portList;
	static CommPortIdentifier portId;
	static String messageString = "Hello, world!\n";
	static SerialPort serialPort;
	static OutputStream outputStream;
	static int PAGECOUNT = 1;
	// 用于过滤全角字符
	private static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("，",",");
	    map.put("。",".");
		map.put("〈","<");
		map.put("〉",">");
		map.put("‖","|");
		map.put("《","<");
		map.put("》",">");
		map.put("〔","(");
		map.put("〕",")");
		map.put("﹖","?");
		map.put("？","?");
		map.put("“","\"");
		map.put("”","\"");
		map.put("：",":");
		map.put("、",",");
		map.put("（","(");
		map.put("）",")");
		map.put("【","[");
		map.put("】","]");
		map.put("—","-");
		map.put("～","~");
		map.put("！","!");
		map.put("‵","'");
		map.put("①","1");
		map.put("②","2");
		map.put("③","3");
		map.put("④","4");
		map.put("⑤","5");
		map.put("⑥","6");
		map.put("⑦","7");
		map.put("⑧","8");
		map.put("⑨","9");
	}

	public static void main(String[] args) {
		try {

			// int width = 128;
			// int height = 64;
			// // 创建BufferedImage对象
			// Font font = new Font("宋体", Font.PLAIN, 16);
			// BufferedImage image = new BufferedImage(width, height,
			// BufferedImage.TYPE_INT_RGB);
			// // 获取Graphics2D
			// Graphics2D g2d = image.createGraphics();
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			// // 画图
			// g2d.setBackground(new Color(255, 255, 255));
			// g2d.setPaint(new Color(0, 0, 0));
			// g2d.clearRect(0, 0, width, height);
			// g2d.drawString("名称：娃哈哈纯净水", 0, 10);
			// g2d.drawString("产地：浙江杭州", 0, 26);
			// g2d.drawString("品牌：娃娃哈哈", 0, 42);
			// g2d.drawString("单价：9876543210", 0, 58);
			// g2d.setFont(font);
			// // 释放对象
			// g2d.dispose();
			// // 保存文件
			// // ImageIO.write(image, "png", new File("D:/print/test.png"));
			// // 文件写入到ByteArrayOutputStream中
			// ImageIO.write(image, "bmp", out);

			portList = CommPortIdentifier.getPortIdentifiers();

			while (portList.hasMoreElements()) {
				portId = (CommPortIdentifier) portList.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (portId.getName().equals("COM5")) {
						// if (portId.getName().equals("/dev/term/a")) {
						try {
							serialPort = (SerialPort) portId.open(
									"SimpleWriteApp", 3000);
						} catch (PortInUseException e) {
						}
						try {
							outputStream = serialPort.getOutputStream();
						} catch (IOException e) {
						}
						try {
							serialPort.setSerialPortParams(19200,
									SerialPort.DATABITS_8,
									SerialPort.STOPBITS_1,
									SerialPort.PARITY_NONE);
						} catch (UnsupportedCommOperationException e) {
						}
						try {
							// outputStream.write(messageString.getBytes());
							// tonsincsPrinterImage(image, image.getHeight());
							// tonsinsPrnterBackPaper();
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("   中国移动通信集团广东有限公司广州分公司");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("   中信服务厅");
							tonsinsPrnterLF();
							tonsinsPrnterSetFont((byte) 2, (byte) 2);
							tonsinsPrnterPrintLine("     综合业务                                      A0001");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("     受理窗口：1、2、3");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("     前面等待人数:20");
							tonsinsPrnterSetFont((byte) 1, (byte) 2);
							tonsinsPrnterPrintLine("________________________________________________");
							tonsinsPrnterLF();
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("你可以登录门户网站(http://www.gd");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("chinamobile.com/gx/)的优惠资讯栏目，");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("随时随地查询各类优惠活动的资讯。");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("------------------------------------------------");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("主动营销信息......");
							tonsinsPrnterSetFont((byte) 1, (byte) 2);
							tonsinsPrnterPrintLine("________________________________________________");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("取号时间：2008-11-30 09：23：50");
							tonsinsPrnterSetFont((byte) 1, (byte) 1);
							tonsinsPrnterPrintLine("取票手机：139xxxxxxxxxxx,请您凭号服务");
							tonsinsPrnterLF();
							tonsinsPrnterLF();
							tonsinsPrnterCutPaper();
						} catch (IOException e) {
						}
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @Title: tonsincsPrinterImage
	 * @Description: TODO(将一个图片字节对象、打印出来)
	 * @param @param image
	 * @param @param hh
	 * @return void 返回类型
	 * @throws IOException
	 */
	public static void tonsincsPrinterImage(BufferedImage image, int hh)
			throws IOException {
		int pageheight;
		int xPos, yPos, mPosx, findex = 0;
		byte b, b1;
		byte GetRed;
		// fs:TFileStream;
		byte xl, xh, yl, yh;
		byte[] databuff = new byte[1024 * 100];
		// bm:tbitmap;
		// pagewidth = 640;
		pageheight = hh;
		// hh=image.getHeight();
		// pagewidth=image.getWidth();
		// 获取图片参数
		// int imageType = image.getType();
		int w = image.getWidth();
		int h = image.getHeight();
		int startX = 0;
		int startY = 0;
		int offset = 0;
		int scansize = w;
		// rgb的数组
		int[] rgbArray = new int[offset + (h - startY) * scansize
				+ (w - startX)];
		image.getRGB(startX, startY, w, h, rgbArray, offset, scansize);

		for (yPos = 0; yPos < h; yPos++) {
			for (xPos = 0; xPos < (w % 8); xPos++) {
				b1 = 01;
				b = 0;
				for (mPosx = xPos * 8 - 1; mPosx < xPos * 8 - 8; mPosx--) {
					// GetRed = (byte) image.getRGB(mPosx, yPos);
					int rgb = rgbArray[offset + (yPos - startY) * scansize
							+ (xPos - startX)];
					Color c = new Color(rgb);
					GetRed = (byte) c.getRed();
					if (GetRed == 0) {
						b = (byte) (b | b1);
						b1 = (byte) (b1 << 1);
					}
				}
				databuff[findex] = b;
				findex++;
			}

		}
		xl = 80;
		xh = 0;
		yl = (byte) (h & 0x00ff);
		yh = (byte) (h >> 8 & 0x00ff);
		for (int j = 0; j < PAGECOUNT; j++) {
			tonsinsPrnterBackPaper();
			tonsinsPrnterLF();
			SettonsinsPrnterNd();
			tonsincsPrnterSendImage(xl, xh, yl, yh, findex, databuff);
			tonsinsPrnterLF();
			tonsinsPrnterLF();
			tonsinsPrnterLF();
			tonsinsPrnterCutPaper();
		}
	}

	public static void tonsincsPrnterSendImage(byte xl, byte xh, byte yl,
			byte yh, int findex, byte[] databuff) throws IOException {
		byte[] sbuff = new byte[8];
		byte[] sendbuff = new byte[63];
		int sendsize;
		byte sendflag;

		sbuff[0] = 0x1d;
		sbuff[1] = 0x76;
		sbuff[2] = 0x30;
		sbuff[3] = 0;
		sbuff[4] = xl;
		sbuff[5] = xh;
		sbuff[6] = yl;
		sbuff[7] = yh;
		sendData(sbuff, sbuff.length);
		sendsize = 0;
		for (int i = 0; i < findex; i++) {
			sendbuff[sendsize] = databuff[i];
			sendsize = sendsize + 1;
			if (sendsize == 64) {
				while (true) {
					sendflag = reciveData();
					if (sendflag == 0)
						break;
					if (sendflag == 128)
						break;
					if (sendflag == 0x11)
						break;
					if (sendflag == 0x13)
						continue;
				}
				sendData(sendbuff, sendsize);
				sendsize = 0;
			}

		}
		if (sendsize != 0) {
			sendData(sendbuff, sendsize);
			sendsize = 0;
		}

	}

	private static byte reciveData() {
		// TODO Auto-generated method stub

		return (byte) 128;
	}

	/**
	 * @Title: tonsinsPrnterCutPaper
	 * @Description: TODO(控制打印机切纸)
	 * @param
	 * @return void 返回类型
	 * @throws IOException
	 */
	public static void tonsinsPrnterCutPaper() throws IOException {
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
	public static void tonsinsPrnterBackPaper() throws IOException {
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
	public static void tonsinsPrnterLF() throws IOException {
		byte[] sbuff = new byte[1];
		sbuff[0] = 0x0a;
		sendData(sbuff, sbuff.length);
	}

	public static void SettonsinsPrnterNd() throws IOException {
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
	public static void tonsinsPrnterSetFont(byte wf, byte hf)
			throws IOException {
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
	public static void tonsincsPrinterPrintAbsoluteFix(String contex, int x,
			int y) throws IOException {
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
	public static String strreplace(String content) {
		int length = content.length();
        for (int i = 0; i < length; i++)
        {
            String charat = content.substring(i, i + 1);
            if (map.containsKey(charat))
            {
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
	public static void tonsinsPrnterPrintLine(String contex) throws IOException {
		byte[] sbuff = new byte[1];
		contex = strreplace(contex);//中英字符转换处理
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
	 * @Title: sendData
	 * @Description: TODO(用于发送字节数据函数)
	 * @param @param sbuff
	 * @param @param bufflen
	 * @return boolean 返回类型
	 * @throws IOException
	 */
	public static void sendData(byte[] sbuff, int bufflen) throws IOException {
		if (outputStream != null) {
			outputStream.write(sbuff);
		}
	}

}
