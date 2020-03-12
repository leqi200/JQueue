package com.tonsincs.task;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.net.JQ_ClientSocket;
import com.tonsincs.net.NClient;
import com.tonsincs.util.OS_Util;

/**
 * @ProjectName:JQueue
 * @ClassName: JQ_Heartbeat
 * @Description: TODO(排队系统的心跳组件用于向服务器发起心跳请求，目前使用的通信协议采用TCP方式)
 * @author 萧达光
 * @date 2014-5-28 下午01:23:34
 * 
 * @version V1.0
 */
public class JQ_Heartbeat extends TimerTask {
	private static Logger log = Logger.getLogger(JQ_Heartbeat.class);

	@Override
	public void run() {
		// 用于接收服务器返包数据
		PG_Package rep_pg = null;
		PG_Package req_pg = new PG_Package();
		try {

			// 获取服务器的IP
			String ip = JQ_Main.OS_CONTEXT.get("REMOTE_IP");
			// 获取服务器的端号口
			int port = Integer.parseInt(JQ_Main.OS_CONTEXT.get("REMOTE_PORT"));
			// 获取营业厅的渠道号
			String channelNo = JQ_Main.OS_CONTEXT.get("CHANNEL_NO");
			// 初始化包参数
			// 包的长度[16+数据体字符长度+1(0结束符的1个字节)]
			int send_data_len = 17 + channelNo.getBytes().length;
			req_pg = new PG_Package(send_data_len, Sys_Constant.GET_PARAMETER,
					0, 0, 0, channelNo);
			log.info("取参数设置请求包：" + req_pg);
			// 创建Socket 客户端
			// NClient nc = new NClient(ip, port);
			// 发送消息到服务器
			// rep_pg = nc.sendMsg(rep_pg);
			JQ_ClientSocket jc = new JQ_ClientSocket(ip, port,
					Sys_Constant.SOCKET_OUT_TIME);
			rep_pg = jc.sendMsg(req_pg);
			System.out.println("打印心跳响应的数据包：" + rep_pg);
			log.info("打印心跳响应的数据包：" + rep_pg);
			doServerParameInstall(rep_pg);
		} catch (IOException e) {
			log.error("",e);
		}

	}

	/**
	 * @Title: doServerParameInstall
	 * @Description: TODO(处理服务器返回取参数设置)
	 * @param @param pg
	 * @return void 返回类型
	 * @throws IOException
	 */
	public void doServerParameInstall(PG_Package pg) throws IOException {
		if (pg==null) {
			return;
		}
		switch (pg.getCmdID()) {
		case Sys_Constant.GET_PARAMETER_REPLY:
			if (pg.getCmdStatus() == 0) { // 只有操作成功才执行修改
				/*
				 * 数据体: 　营业厅渠道号~号码强制标识(0:不强制
				 * 或1：强制)~系统时间（ＹＹＹＹ-ＭＭ-ＤＤ　ＨＨ:ＭＩ:ＳＳ）~无需手机号码输入选项
				 * (业务类型1:名称1;业务类型２:名称２)~ 电子屏显示内容~是否需要取号确认（1为是，0为否）
				 */
				String[] pramter = pg.getBody().split(Sys_Constant.DELIMITER);// 分割参数
				// 修改一些系统参数
				String[] key = { "FORCE_NUMBER", "NO_PHONE_INPUT_OPTION_SERVER",
						"LED_CONTENT", "TAKE_NUMBER_VALIDATION" };
				String[] value =null;
				if (pramter.length>4) {
					value=new String[]{ pramter[1], pramter[3], pramter[4],
							pramter[5] };
				}else {
					value =new String[] { pramter[1], pramter[3], "欢迎您的光临",
							"1"};
				}
				// 修改Windows时间
				Runtime.getRuntime().exec(
						"cmd /c date " + pramter[2].substring(0, 10));// Windows系统
				Runtime.getRuntime().exec(
						"cmd /c time " + pramter[2].substring(10, 19));// Windows系统
				// 更新系统当前状态值
				OS_Util.updateOsContext(key, value);// 更新系统参数函数
			} else {
				log.info("渠道标识不对");
			}
			break;

		default:
			log.info("无效指令" + pg.getCmdID());
			break;
		}
	}
}
