package com.tonsincs.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Telnet 客户端工具类
 * 
 * @author dellb
 *
 */
public class TelnetClientUtils {

	/**
	 * 测试某个商品是否是打开状态
	 * 
	 * @param ip目标主机
	 * @param port指定端口
	 * @throws IOException
	 */
	public static void telnetPort(String ip, int port) throws IOException {
		Socket telCline = new Socket();
		InetSocketAddress address = new InetSocketAddress(ip, port);
		telCline.connect(address, 0);
		// telCline.connect(address);
		telCline.close();
		telCline = null;

	}

	public static void main(String[] args) throws IOException {
		System.out.println("aaaaa");
		telnetPort("192.168.10.101", 12345);
		System.out.println("bbbb");
	}

}
