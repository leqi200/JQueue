package com.tonsincs.entity;

import java.io.UnsupportedEncodingException;

import com.tonsincs.constant.Sys_Constant;

/**
* @ProjectName:JQueue
* @ClassName: PG_Package
* @Description: TODO(根据康腾科技提供的数据包格式建立的一个数据 POJO对象)
* @author 萧达光
* @date 2014-5-28 下午01:42:26
* 
* @version V1.0 
*/
public class PG_Package {
	// 数据包的大小：不包括自身的数据包长度，也就是(数据包的总长度-4)
	private int pktLength;
	// 指令
	private int cmdID;
	// 指令执行状态
	private int cmdStatus;
	// 流水号，由发起方指定，回应包采用同一序列号回复
	private int serialNo;
	// 保留字段
	private int reserverd;
	/*
	 * 不定长，是以NULL结尾(即'\0'); 的字符串 具体的数据体，数据体中每个字段这间以"~"符号作为分隔符
	 */
	private String body = null;
	/**
	 * @Fields buf : TODO(用于获取包装后的字节数组)
	 */
	private byte[] buf = null;

	/**
	 * 将int转为字节数组采用大端法
	 */
	private static byte[] intToByte(int n) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((n >> 24) & 0xFF);
		result[1] = (byte) ((n >> 16) & 0xFF);
		result[2] = (byte) ((n >> 8) & 0xFF);
		result[3] = (byte) (n & 0xFF);
		return result;

	}

	public PG_Package() {
	}

	/**
	 * 创建一个不带body 的数据
	 * 
	 * @param pktLength
	 *            数据包大小
	 * @param cmdID
	 *            指令
	 * @param cmdStatus指令执行状态
	 * @param serialNo流水号
	 * @param reserverd
	 *            保留字段
	 */
	public PG_Package(int pktLength, int cmdID, int cmdStatus, int serialNo,
			int reserverd) {
		super();
		this.pktLength = pktLength;
		this.cmdID = cmdID;
		this.cmdStatus = cmdStatus;
		this.serialNo = serialNo;
		this.reserverd = reserverd;
		//初始化字节数组
		byte[] temp = null;
		buf = new byte[pktLength + 4];// 加4是用于保存包的长度
		temp = intToByte(pktLength);
		System.arraycopy(temp, 0, buf, 0, temp.length);// 这个方法是复制byte字节到一个指定的数组位置中俏存放
		temp = intToByte(cmdID);
		System.arraycopy(temp, 0, buf, 4, temp.length);
		temp = intToByte(cmdStatus);
		System.arraycopy(temp, 0, buf, 8, temp.length);
		temp = intToByte(serialNo);
		System.arraycopy(temp, 0, buf, 12, temp.length);
		temp = intToByte(reserverd);
		System.arraycopy(temp, 0, buf, 16, temp.length);

	}

	/**
	 * 带数据体的构造函数
	 * 
	 * @param pktLength
	 *            数据包大小
	 * @param cmdID
	 *            指令
	 * @param cmdStatus指令执行状态
	 * @param serialNo流水号
	 * @param reserverd
	 *            保留字段
	 * @param body
	 *            数据体
	 * @throws UnsupportedEncodingException
	 */
	public PG_Package(int pktLength, int cmdID, int cmdStatus, int serialNo,
			int reserverd, String body) throws UnsupportedEncodingException {
		super();
		this.pktLength = pktLength;
		this.cmdID = cmdID;
		this.cmdStatus = cmdStatus;
		this.serialNo = serialNo;
		this.reserverd = reserverd;
		this.body = body;
		// 初始化字节数组
		byte[] temp = null;
		buf = new byte[pktLength + 4];// 加上4是用于保存包的长度头长度，与实际包的长度是不一样的
		temp = intToByte(pktLength);
		System.arraycopy(temp, 0, buf, 0, temp.length);// 这个方法是复制byte字节到一个指定的数组位置中俏存放
		temp = intToByte(cmdID);
		System.arraycopy(temp, 0, buf, 4, temp.length);
		temp = intToByte(cmdStatus);
		System.arraycopy(temp, 0, buf, 8, temp.length);
		temp = intToByte(serialNo);
		System.arraycopy(temp, 0, buf, 12, temp.length);
		temp = intToByte(reserverd);
		System.arraycopy(temp, 0, buf, 16, temp.length);
		if (body == null || body.equals("")) {
			temp = new byte[] {0};// 空字节
		} else {
			temp = body.getBytes(Sys_Constant.SYS_DEFAULT_ENCODED);
		}
		System.arraycopy(temp, 0, buf, 20, temp.length);
	}

	// 属性封装字段
	public int getPktLength() {
		return pktLength;
	}

	public void setPktLength(int pktLength) {
		this.pktLength = pktLength;
	}

	public int getCmdID() {
		return cmdID;
	}

	public void setCmdID(int cmdID) {
		this.cmdID = cmdID;
	}

	public int getCmdStatus() {
		return cmdStatus;
	}

	public void setCmdStatus(int cmdStatus) {
		this.cmdStatus = cmdStatus;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public int getReserverd() {
		return reserverd;
	}

	public void setReserverd(int reserverd) {
		this.reserverd = reserverd;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public byte[] getBuf() {
		return buf;
	}

	public void setBuf(byte[] buf) {
		this.buf = buf;
	}

	@Override
	public String toString() {
		return "PG_Package [body[数据体]=" + body + ", cmdID[指令]=" + cmdID + ", cmdStatus[指令状态]="
				+ cmdStatus + ", pktLength[包的长度]=" + pktLength + ", reserverd="
				+ reserverd + ", serialNo=" + serialNo + "]";
	}

}
