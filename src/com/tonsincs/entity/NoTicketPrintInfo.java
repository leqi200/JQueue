package com.tonsincs.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.main.JQ_Main;

/**
* @ProjectName:JQueue
* @ClassName: NoTicketPrintInfo
* @Description: TODO(这一个是没有带票号的打印样式)
* @author 萧达光
* @date 2014-5-28 下午01:41:30
* 
* @version V1.0 
*/
public final class NoTicketPrintInfo extends Ticket {
	private int PAGES = 0;
	private String printStr;
	private String[] printdata = null;

	public NoTicketPrintInfo(String printStr) {
		super(printStr);
		if (printStr != null) {
			// 以分割符为"~",分割字符串
			this.printdata = printStr.split(Sys_Constant.DELIMITER);
		}
	}

	/*
	 * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，
	 * 1点为1英寸的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号
	 */
	@Override
	public int print(Graphics gp, PageFormat pf, int page)
			throws PrinterException {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) gp;
		g2.setPaint(Color.black); // 设置打印颜色为黑色
		if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束
			return Printable.NO_SUCH_PAGE;
		double x = pf.getImageableX();
		double y = pf.getImageableY();
		g2.translate(x, y);// 转换坐标，确定打印边界

		// 创建字体系统用到的字体
		Font fon_8 = new Font("宋体", Font.PLAIN, 8);
		Font fon_9 = new Font("宋体", Font.PLAIN, 9);
		Font fon_10 = new Font("宋体", Font.PLAIN, 10);
		Font fon_14 = new Font("宋体", Font.PLAIN, 14);

		// 设置每种格式字体的高度
		float heigthl_8 = fon_8.getSize2D();
		float heigthl_9 = fon_9.getSize2D();
		float heigthl_10 = fon_10.getSize2D();
		float heigthl_14 = fon_14.getSize2D();

		//
		g2.setFont(fon_8);
		// 打印当前页文本
		// int printFontCount = printStr.length();// 打印字数
		// int printFontSize = fon_8.getSize();// Font 的磅值大小
		// float printX = 80 / 2; // 给定字符点阵，X页面正中
		// float printY = 50 / 2; // 给定字符点阵，Y页面正中
		// float printMX = printX - (printFontCount * printFontSize / 2);//
		// 打印到正中间
		// float printMY = printY - printFontSize / 2;// 打印到正中间

		// 开始打印公司名称标题
		// 具体打印每一行文本，同时走纸移位
		g2.drawString(JQ_Main.OS_CONTEXT.get("COMPANY_TITLE") + printdata[1],
				(float) x + 10, (float) y + 1 * heigthl_8);
		// 打印营业厅
		g2.drawString(printdata[2], (float) x + 10, (float) y + 2 * heigthl_8);
		//
		return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
	}

	public static void main(String[] args) {
		String str="ABCDE~中国移动广州分公司~中信服务厅";
		NoTicketPrintInfo lp = new NoTicketPrintInfo(str);
		lp.printContent();
	}
}
