package com.tonsincs.entity;

import org.apache.log4j.Logger;

/**
* @ProjectName:JQueue
* @ClassName: PrintSuper
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 萧达光
* @date 2014-5-28 下午01:43:51
* 
* @version V1.0 
*/
public abstract class PrintSuper {
	protected static Logger log=Logger.getLogger(PrintSuper.class);
	// 基本参数设置
	protected PrintParameter pp;
	// 基本参数设置
	/**
	 * @Fields company : TODO(公司名称)
	 */
	protected String company = "";
	/**
	 * @Fields sellingArea : TODO(服务厅名称)
	 */
	protected String sellingArea = "";
	/**
	 * @Fields bizname : TODO(业务名称)
	 */
	protected String bizname = "";
	/**
	 * @Fields ticketNo : TODO(票号)
	 */
	protected String ticketNo = "";
	/**
	 * @Fields counter : TODO(窗口号)
	 */
	protected String counter = "";
	/**
	 * @Fields waitNum : TODO(等候人数)
	 */
	protected String waitNum = "";
	/**
	 * @Fields content : TODO(补充内容)
	 */
	protected String content = "";
	/**
	 * @Fields marketing : TODO(营销内容)
	 */
	protected String marketing = "";
	/**
	 * @Fields ticketTime : TODO(取号时间)
	 */
	protected String ticketTime = "";
	/**
	 * @Fields phone : TODO(手机号码)
	 */
	protected String phone = "";
	/**
	 * @Fields channelNo : TODO(渠道编号)
	 */
	protected String channelNo = "";
	/**
	 * @Fields bizNo : TODO(业务编码)
	 */
	protected String bizNo = "";

	/**
	 * @Fields printStr : TODO(打印的全部内容)
	 */
	protected String printStr;
	/**
	 * @Fields printdata : TODO(用于接收每一个参数内容)
	 */
	protected String[] printdata = null;

	
	
	public PrintSuper(String printStr) {
		this.printStr = printStr;
	}

	/**
	 * @Title: print
	 * @Description: TODO(打印方法票号的抽象方法)
	 * @param
	 * @return void 返回类型
	 */
	abstract public void print();

	/**
	 * @Title: splitting
	 * @Description: TODO(将一大段文本，按指定字数拆分，以数组的形式返回)
	 * @param @param content 要拆分的内容
	 * @param @param num 指定拆分字数
	 * @param @return
	 * @return String[] 返回类型
	 */
	protected static String[] splitting(String content, int num) {
		if (content == null) {
			return null;
		}
		int len = content.length();
		int lines = (len % num) == 0 ? (len / num) : (len / num) + 1;// 计算行数
		String[] result = new String[lines];
		int getlen = 0;
		for (int i = 0; i < result.length; i++) {
			String temp = "";
			if (len > num) {
				temp = content.substring(0, num);// 每一次最字符串里面取一定长度的数据
				result[i] = temp;
				getlen = temp.length();
			} else {
				temp = content.substring(0, len);// 每一次最字符串里面取一定长度的数据
				result[i] = temp;
				getlen = temp.length();
			}
			content = content.substring(getlen, content.length());// 重新指定新的字符串
			len = content.length();
		}
		return result;
	}

	// public static void main(String[] args) {
	// String str = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaas";
	// String[] aa = splitting(str, 20);
	// for (int i = 0; i < aa.length; i++) {
	// System.out.println(aa[i]);
	// }
	// }
}
