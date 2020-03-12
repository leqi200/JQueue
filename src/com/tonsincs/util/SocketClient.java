package com.tonsincs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;

/**
* @ProjectName:JQueue
* @ClassName: SocketClient
* @Description: TODO(客户端组件)
* @author 萧达光
* @date 2014-5-28 下午01:32:19
* 
* @version V1.0 
*/
public class SocketClient {
	private String hostName;
	private int portNum;
	private int delaySecond; // 发文接收返回报文延时
	// 定义处理编码和解码的字符集
	private Charset charset = Charset.forName(Sys_Constant.SYS_DEFAULT_ENCODED);
	private static Logger log = Logger.getLogger(SocketClient.class);

	/**
	 * @Description: TODO
	 * @param hostName
	 *            语音服务主机IP地址
	 * @param portNum
	 *            端口号
	 * @param delaySecond
	 *            延时时间单位毫秒
	 */
	public SocketClient(String hostName, int portNum, int delaySecond) {
		super();
		this.hostName = hostName;
		this.portNum = portNum;
		this.delaySecond = delaySecond;
	}

	public SocketClient() {
		this.hostName ="192.168.18.86";
		this.portNum = 7777;
		this.delaySecond = 2000;
		// pFileOp = null;
	}

	private Socket getSocket() {
		Socket socket = null;
		try {
			socket = new Socket(hostName, portNum);
		} catch (UnknownHostException e) {
			log.error("-->未知的主机名:" + hostName + " 异常");
		} catch (IOException e) {
			log.error("-hostName=" + hostName + " portNum=" + portNum
					+ "---->IO异常错误" + e.getMessage());
		}
		return socket;
	}

	public PG_Package sendMessage(PG_Package pg){
		PG_Package pack = null; // 用于接收返回来的PG_Package数据
		if (pg!=null) {
			// 根据PG包的长度创建一个只定长度的节点包
			ByteBuffer send = ByteBuffer.allocate(pg.getPktLength());
			send.putInt(send.capacity());
			send.putInt(pg.getCmdID());
			send.putInt(pg.getCmdStatus());
			send.putInt(pg.getSerialNo());
			send.putInt(pg.getReserverd());
			if (pg.getBody() == null || pg.getBody().isEmpty()) {
				// 如果为空则发送0字节结束
				send.put((byte) 0);
			} else {
				// 如果不为空、则将发送数据转换成字节数组再发送
				ByteBuffer bb = charset.encode(pg.getBody());
				send.put(bb);
				send.put((byte) 0);
			}
			// 重置数据指针
			send.flip();
			
		}
		return null;
	}
	
	public String sendMessage(String strMessage) {
		String str = "";
		String serverString = "";
		Socket socket;
		try {
			socket = getSocket();
			// socket.setKeepAlive(true);
			if (socket == null) { // 未能得到指定的Socket对象,Socket通讯为空
				return "200";
			}
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			log.info("---->发送报文=" + strMessage);
			out.println(strMessage);
			out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			long sendTime = System.currentTimeMillis();
			long receiveTime = System.currentTimeMillis();
			boolean received = false; // 成功接收报文
			boolean delayTooLong = false;
			serverString = null;
			while (!received && !delayTooLong) {
				if (socket.getInputStream().available() > 0) {
					// serverString = in.readLine();
					char tagChar[];
					tagChar = new char[1024];
					int len;
					String temp;
					String rev = "";
					if ((len = in.read(tagChar)) != -1) {
						temp = new String(tagChar, 0, len);
						rev += temp;
						temp = null;
					}
					serverString = rev;
				}
				receiveTime = System.currentTimeMillis();
				if (serverString != null)
					received = true; // 字符串不为空，接收成功
				if ((receiveTime - sendTime) > delaySecond)
					delayTooLong = true; // 接收等待时间过长，超时
			}
			in.close();
			out.close();
			str = serverString;
			if (delayTooLong)
				str = "200";; // 超时标志为真，返回超时码
			if (!received)
				str = "200";
			socket.close();
		} catch (UnknownHostException e) {
			log.error("---->出现未知主机错误! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			str = "200";
			// System.exit(1);
		} catch (IOException e) {
			log.error("---->出现IO异常! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			e.printStackTrace();
			str ="200";
		} catch (Exception e) {
			str = "200";
			log.error("---->出现未知异常" + e.getMessage());
		} finally {
			socket = null;
			str.trim();
			log.error("--->返回的socket通讯字符串=" + str);
		}
		return str;
	}

	// public static void main(String[] args) {
	// SocketClient sc = new SocketClient();
	// sc.sendMessage("切实");
	// }
}
