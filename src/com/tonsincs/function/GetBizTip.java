package com.tonsincs.function;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Display;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.net.JQ_ClientSocket;
import com.tonsincs.net.NClient;
import com.tonsincs.util.OS_Util;

/**
* @ProjectName:JQueue
* @ClassName: GetBizTip
* @Description: TODO(取业务提示函数)
* @author 萧达光
* @date 2014-5-28 下午01:39:29
* 
* @version V1.0 
*/
public class GetBizTip extends BrowserFunction {
	private static Logger log = Logger.getLogger(GetBizTip.class);
	private Browser browser;

	public GetBizTip(Browser browser, String name) {
		super(browser, name);
		// TODO Auto-generated constructor stub
		this.browser = browser;
	}

	/**
	 * @Title: function
	 * @Description: TODO(请求业务提示)
	 * @param @param argument 方法接收的参数[基本要求参数有：业务类型代号]
	 * @param @return
	 * @return Object 返回类型
	 */
	@Override
	public Object function(final Object[] arguments) {
		// TODO Auto-generated method stub
		Display display = Display.getDefault();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					String channel_no = JQ_Main.OS_CONTEXT.get("CHANNEL_NO");// 获取渠道号
					String bizNo = (String) arguments[0];
					String fun_Name = (String) arguments[1]; // 函数名称
	
					String send_data = channel_no +Sys_Constant.DELIMITER + bizNo;
					// 初始化提交包参数
					int send_data_len=17+send_data.getBytes().length;
					PG_Package pg=new PG_Package(send_data_len, Sys_Constant.GET_SERVICE_HINT, 0, 0, 0, send_data);
					JQ_ClientSocket jc = new JQ_ClientSocket(JQ_Main.OS_CONTEXT.get("REMOTE_IP"),
							Integer.parseInt(JQ_Main.OS_CONTEXT
									.get("REMOTE_PORT")),Sys_Constant.SOCKET_OUT_TIME);
					PG_Package pack = jc.sendMsg(pg);
					log.info("打印返回包的信息："+pack);
					String msg = "";
					int flag = 0;
					if (pack != null && pack.getCmdStatus() == 0) {
						msg = pack.getBody();
						flag = 0;
					} else if (pack != null && pack.getCmdStatus() == 1) {
						msg = "排队机营业厅编号设置有误,无些营业厅编号";
						flag = 1;
					} else if (pack != null && pack.getCmdStatus() == 2) {
						msg = "错误的业务类型代码";
						flag = 2;
					} else if (pack != null && pack.getCmdStatus() == 3) {
						msg = "无相关业务提示";
						flag = 3;
					} else {
						msg = "未知异常";
						log.error(msg);
						flag = Sys_Constant.CR_CALLBACK_FAIL;
					}
					browser.execute(fun_Name + "('" + flag + "','" + msg
									+ "')");
					// String msg="这一个业务是XXXX的业务";
					// browser.execute(fun_Name + "('" + 0 + "','" + msg
					// + "')");

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					log.error("",e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("",e);
				}

			}

		});
		return 0;
	}

}
