package com.tonsincs.interfaces;

import java.awt.print.Printable;

/**
* @ProjectName:JQueue
* @ClassName: Ticket
* @Description: TODO(票号打印接口抽象定义一些打印基本参数和打印方法)
* @author 萧达光
* @date 2014-5-28 下午01:38:13
* 
* @version V1.0 
*/
public interface Ticket extends Printable {
	public String company="";//公司名称
	public String sellingArea="";//服务厅名称
	public String bizname="";//业务名称
	public String ticketNo="";//票号
	public String counter="";//窗口号
	public String waitNum="";//等候人数
	public String content="";//补充内容
	public String marketing="";//营销内容
	public String ticketTime="";//取号时间
	public String phone="";//手机号码
	
	public void printContent();//打印内容
}
