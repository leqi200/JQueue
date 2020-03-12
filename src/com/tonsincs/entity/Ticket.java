package com.tonsincs.entity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.print.Printable;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JOptionPane;

/**
* @ProjectName:JQueue
* @ClassName: Ticket
* @Description: TODO(打印票号抽象类)
* @author 萧达光
* @date 2014-5-28 下午01:44:11
* 
* @version V1.0 
*/
public abstract class Ticket implements Printable {
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
	 * @Fields PAGES : TODO(页数)
	 */
	protected static int PAGES = 0;
	/**
	 * @Fields printStr : TODO(打印的全部内容)
	 */
	protected String printStr;
	/**
	 * @Fields printdata : TODO(用于接收每一个参数内容)
	 */
	protected String[] printdata = null;

	// 字体定义格式
	// 创建字体系统用到的字体
	protected Font fon_8 = new Font("宋体", Font.PLAIN, 8);
	protected Font fon_9 = new Font("宋体", Font.PLAIN, 9);
	protected Font fon_10 = new Font("宋体", Font.BOLD, 10);
	protected Font fon_14 = new Font("宋体", Font.BOLD, 14);

	// 设置每种格式字体的高度
	protected float heigthl_8 = fon_8.getSize2D();
	protected float heigthl_9 = fon_9.getSize2D();
	protected float heigthl_10 = fon_10.getSize2D();
	protected float heigthl_14 = fon_14.getSize2D();

	public Ticket(String printStr) {
		this.printStr = printStr;
	}

	/**
	 * @Title: printContent
	 * @Description: TODO(打印函数)
	 * @param
	 * @return void 返回类型
	 */
	protected void printContent() {
		if (printStr != null && printStr.length() > 0) // 当打印内容不为空时
		{
			PAGES = 1; // 获取打印总页数
			// 指定打印输出格式
			DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
			// 定位默认的打印服务
			PrintService printService = PrintServiceLookup
					.lookupDefaultPrintService();
			// 创建打印作业
			DocPrintJob job = printService.createPrintJob();
			// 设置打印属性
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			// 设置纸张大小,也可以新建MediaSize类来自定义大小
			pras.add(MediaSizeName.ISO_A4);
			DocAttributeSet das = new HashDocAttributeSet();
			// 指定打印内容
			Doc doc = new SimpleDoc(this, flavor, das);
			// 不显示打印对话框，直接进行打印工作
			try {
				job.print(doc, pras); // 进行每一页的具体打印操作
			} catch (PrintException pe) {
				pe.printStackTrace();
			}
		} else {
			// 如果打印内容为空时，提示用户打印将取消
			JOptionPane.showConfirmDialog(null,
					"Sorry, Printer Job is Empty, Print Cancelled!",

					"Empty", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @Title: drawText
	 * @Description: TODO(将打印目标文本并且实现智能换行)
	 * @param @param str 要打印的具体内容
	 * @param @param gp 引用打印对象
	 * @return void 返回类型
	 */
	protected void drawText(String str, Graphics2D gp) {
		String[] drawText = new String[1];// 初始化数组
		for (int i = 0; i < 1; i++) {
			drawText[i] = ""; // 数组元素初始化为空字符串
		}
		int k, suffix = 0, lines = 0;
		while (str.length() > 0) {
			if (lines < 54) // 不够一页时
			{
				k = str.indexOf('\n');
				if (k != -1) // 存在回车符
				{
					lines += 1; // 行数累加
					// 计算该页的具体文本内容，存放到相应下标的数组元素
					drawText[suffix] = drawText[suffix]
							+ str.substring(0, k + 1);
					if (str.substring(k + 1).length() > 0)
						str = str.substring(k + 1);
				} else {
					lines += 1; // 行数累加
					// 将文本内容存放到相应的数组元素
					drawText[suffix] = drawText[suffix] + str;
					str = "";
				}
			} else // 已满一页时
			{
				lines = 0; // 行数统计清零
				suffix++; // 数组下标加 1
			}
		}
	}

}
