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
* @ClassName: GetTicketNumber
* @Description: TODO(请求票号,并且实现打印功能)
* @author MrXiao
* @date 2014-5-28 下午01:39:42
* 
* @version V1.0 
*/
public class GetTicketNumber extends BrowserFunction {
	private Browser browser;
	private static Logger log=Logger.getLogger(GetTicketNumber.class);

	public GetTicketNumber(Browser browser, String name) {
		super(browser, name);
		// TODO Auto-generated constructor stub
		this.browser = browser;
	}
	
	/**
	 * @Title: function
	 * @Description: TODO(请求票号请求)
	 * @param @param argument
	 *        方法接收的参数[基本要求参数有：业务类型、手机号码（注意，如果新入网用户没有手机号码传入一个‘新入网用户’字符串
	 *        ,否则程序会出现异常）、回调处理函数名称]
	 * @param @return 
	 * @return Object 返回类型
	 */
	@Override
	public Object function(final Object[] arguments) {
		Display display = Display.getDefault();
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					// 获取函数参数
					String biz_Type = (String) arguments[0]; // 业务类型
					String number = (String) arguments[1]; // 手机号码
					String fun_Name = (String) arguments[2]; // 函数名称
					String channel_no = JQ_Main.OS_CONTEXT.get("CHANNEL_NO");// 获取渠道号
					String send_data = channel_no + Sys_Constant.DELIMITER
							+ number + Sys_Constant.DELIMITER + biz_Type;
					// 初始化提交包参数
					browser.execute("returnOpenLoadDialog()");
					int send_data_len=17+send_data.getBytes().length;
					PG_Package pg=new PG_Package(send_data_len, Sys_Constant.GET_TICKET_NO, 0, 0, 0,send_data);
					JQ_ClientSocket jc = new JQ_ClientSocket(JQ_Main.OS_CONTEXT.get("REMOTE_IP"),
							Integer.parseInt(JQ_Main.OS_CONTEXT
									.get("REMOTE_PORT")),Sys_Constant.SOCKET_OUT_TIME);
					PG_Package pack = jc.sendMsg(pg);
					log.info("打印返回包的信息："+pack);
					String msg = "";
					int flag = 0;
					if (pack != null && pack.getCmdStatus() == 0) {
						msg = "正在打印中,请等候";
						flag = 0;
						// 调用出号函数
						OS_Util.printTicketNo(pack.getBody());
					} else if (pack != null && pack.getCmdStatus() == 1) {
						msg = "营业厅编码设置错误,无此营业厅编号";
						flag = 1;
					} else if (pack != null && pack.getCmdStatus() == 2) {
						msg = "错误的业务类型代码";
						flag = 2;
					} else if (pack != null && pack.getCmdStatus() == 3) {
						msg = "无手机号码";
						flag = 3;
					} else {
						msg = "未知异常";
						log.error(msg);
						flag = Sys_Constant.CR_CALLBACK_FAIL;
					}
					browser.execute("returnCloceLoadDialog()");
					browser.execute(fun_Name + "('" + flag + "','" + msg
									+ "')");
					//String msg="正在打印..";
					//String str= "AAGZ00001~广州分公司~中信营业厅~A~综合业务~X02~5~受理窗口:1、2、3~2014-03-19 16:20:30~你可以登录门户网站(http://www.gdchinamobile.com/gx/)的优惠资讯栏目，随时随地查询各类优惠活动的资讯。~主动营销内容:.............~取票手机:1392103233,请您凭号服务";;
					//OS_Util.printTicketNo(str);
					 //browser.execute(fun_Name + "('" + 0+ "','"+msg+"')");
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					log.error("",e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					log.error("",e);
				}

			}
		});
		return 0;
	}
}
