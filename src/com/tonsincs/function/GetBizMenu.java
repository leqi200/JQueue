package com.tonsincs.function;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Display;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.net.JQ_ClientSocket;
import com.tonsincs.util.OS_Util;

/**
 * @ProjectName:JQueue
 * @ClassName: GetBizMenu
 * @Description: TODO(请求业务选择菜单)
 * @author 萧达光
 * @date 2014-5-28 下午01:38:48
 * 
 * @version V1.0
 */
public class GetBizMenu extends BrowserFunction {
	private Browser browser;
	private static Logger log = Logger.getLogger(GetBizMenu.class);

	public GetBizMenu(Browser browser, String name) {
		super(browser, name);
		// TODO Auto-generated constructor stub
		this.browser = browser;
	}

	/**
	 * @Title: function
	 * @Description: TODO(请求业务选择菜单请求，这个方法有两种情况：1、显示请求返回相关的业务菜单，2、直接打印票号)
	 * @param @param argument
	 *        方法接收的参数[基本要求参数有：号码类型(1、手机;2卡号)、手机号码或VIP卡号、回调处理函数名称]
	 *        (如果是新入网用户则传入'新入网用户'作为手机号码栏)
	 * @param @return
	 * @return Object 返回类型
	 */
	@Override
	public Object function(final Object[] arguments) {
		Display display = Display.getDefault();
		int result = 0;

		// ByteBuffer send=Charset.forName("UTF-8");
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					// 获取函数参数
					String number_Type = (String) arguments[0];
					String number = (String) arguments[1];
					String fun_Name = (String) arguments[2];
					String channel_no = JQ_Main.OS_CONTEXT.get("CHANNEL_NO");// 获取渠道号
					String send_data = channel_no + Sys_Constant.DELIMITER
							+ number_Type + Sys_Constant.DELIMITER + number;
					int send_data_len = 17 + send_data.getBytes().length;
					// 创建字符集
					// Charset charset = Charset
					// .forName(Sys_Constant.SYS_DEFAULT_ENCODED);
					// 初始化提交包参数
					// PG_Package pg = new PG_Package();
					// pg.setCmdID(Sys_Constant.GET_BIZ_MENU);
					// pg.setCmdStatus(0);
					// pg.setSerialNo(1);
					// pg.setReserverd(0);
					// pg.setBody(send_data);
					// pg.setPktLength(charset.encode(send_data).capacity());
					//
					// nClient = new
					// NClient(JQ_Main.OS_CONTEXT.get("REMOTE_IP"),
					// Integer.parseInt(JQ_Main.OS_CONTEXT
					// .get("REMOTE_PORT")));
					PG_Package pg = new PG_Package(send_data_len,
							Sys_Constant.GET_BIZ_MENU, 0, 0, 0, send_data);
					JQ_ClientSocket jc = new JQ_ClientSocket(JQ_Main.OS_CONTEXT
							.get("REMOTE_IP"), Integer
							.parseInt(JQ_Main.OS_CONTEXT.get("REMOTE_PORT")),
							Sys_Constant.SOCKET_OUT_TIME);
					PG_Package pack = jc.sendMsg(pg);
					log.info("打印返回包的信息：" + pack);
					String msg = "";
					boolean isexe = true;
					int flag = 0;
					String ishint = JQ_Main.OS_CONTEXT
							.get("TAKE_NUMBER_VALIDATION");// 是否要请求号票提示
					if (pack != null && pack.getCmdStatus() == 0) {
						switch (pack.getCmdID()) {
						case Sys_Constant.GET_BIZ_MENU_REPLY:
							// 回显相应的业务菜单（响应相应的状态码）
							msg = pack.getBody();
							flag = 0;
							browser.execute(fun_Name + "('"
									+ pack.getCmdStatus() + "','"
									+ pack.getBody() + "','" + ishint + "')");

							isexe = false;
							break;
						case Sys_Constant.GET_TICKET_NO_REPLY:
							// 直接出票号处理(打印票号、)
							// browser.execute("alert('执行打印操作中...')");
							browser.execute("returnResult(0,'正在打印')");
							OS_Util.printTicketNo(pack.getBody());// 调用打印函数
							isexe = false;
							break;
						}
					} else if (pack != null && pack.getCmdStatus() == 1) {
						flag = 1;
						msg = "排队机营业厅编号设置错误,无此营业厅编号";
						log.info(msg);
					} else if (pack != null && pack.getCmdStatus() == 2) {
						flag = 2;
						// 查询BOSS失败
						msg = pack.getBody();
						log.info(msg);
						log.info("查询BOSS失败" + pack);
					} else if (pack != null && pack.getCmdStatus() == 3) {
						flag = 3;
						msg = "错误的手机号码或卡号";
						log.info(msg + pack);

					} else {
						flag = Sys_Constant.CR_CALLBACK_FAIL;
						msg = "未知异常";
						log.error(msg);
					}
					if (isexe) {
						browser.execute(fun_Name + "('" + flag + "','" + msg
								+ "','" + ishint + "')");
					}
					// String msg="AAGZ00001~13800123456~A|综合业务|B|预付费业务";
					// String msg="手机号码不正确";
					// browser.execute(fun_Name + "('" + 0 + "','" + msg
					// + "','" + 1 + "')");
				} catch (NumberFormatException e) {
					log.error("", e);
				} catch (IOException e) {
					log.error("", e);
				}

			}
		});
		return 0;
	}
}
