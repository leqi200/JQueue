package com.tonsincs.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;

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
* @ClassName: LocatePrint
* @Description: TODO(调用本地打印机)
* @author 萧达光
* @date 2014-5-28 下午01:29:27
* 
* @version V1.0 
*/
public class LocatePrint implements Printable {
	private int PAGES = 0;

	private String printStr;

	public LocatePrint(String printStr) {
		super();
		this.printStr = printStr;
	}

	/*
	 * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，
	 * 1点为1英寸的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号
	 */
	public int print(Graphics gp, PageFormat pf, int page)
			throws PrinterException {
		Graphics2D g2 = (Graphics2D) gp;
		g2.setPaint(Color.black); // 设置打印颜色为黑色
		if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束
			return Printable.NO_SUCH_PAGE;
		g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
		Font font = new Font("宋体", Font.PLAIN, 9);// 创建字体
		g2.setFont(font);
		// 打印当前页文本
		int printFontCount = printStr.length();// 打印字数
		int printFontSize = font.getSize();// Font 的磅值大小
		float printX = 80 / 2; // 给定字符点阵，X页面正中
		float printY = 50 / 2; // 给定字符点阵，Y页面正中
		float printMX = printX - (printFontCount * printFontSize / 2);// 打印到正中间
		float printMY = printY - printFontSize / 2;// 打印到正中间
		// g2.drawString(printStr, printMX, printMY); // 具体打印每一行文本，同时走纸移位
		// g2.drawString(printStr, printMX - printFontSize * printFontCount,
		// printMY + printFontSize); // 具体打印每一行文本，同时走纸移位
		// g2.drawString(printStr, printMX + printFontSize * printFontCount,
		// printMY + printFontSize); // 具体打印每一行文本，同时走纸移位
		// g2.drawString(printStr, printMX, printMY + printFontSize * 2); //
		// 具体打印每一行文本，同时走纸移位
		String s = getDrawText(printStr)[page];// 获取当前页的待打印文本内容
		String drawText;
		int k, i = font.getSize(), lines = 0;
		float ascent = 9; // 给定字符点阵
		while (s.length() > 0 && lines < 54) // 每页限定在 54 行以内
		{
			k = s.indexOf('\n'); // 获取每一个回车符的位置
			if (k != -1) // 存在回车符
			{
				lines += 1; // 计算行数
				drawText = s.substring(0, k); // 获取每一行文本
				g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
				//g2.drawString(drawText, printMX, printMY + printFontSize * 2); // 具体打印每一行文本，同时走纸移位
				// g2.drawString(drawText, 0, (float) y + 1 * heigth );
				// g2.drawString(drawText, 200, 200);
				if (s.substring(k + 1).length() > 0) {
					s = s.substring(k + 1); // 截取尚未打印的文本
					ascent += i;
				}
			} else // 不存在回车符
			{
				lines += 1; // 计算行数
				drawText = s; // 获取每一行文本
				g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
				s = ""; // 文本已结束
			}
		}

		return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
	}

	// 打印内容到指定位置
	public void printContent() {
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
			pras.add(MediaSizeName.ISO_A10);
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

	/* 将打印目标文本按页存放为字符串数组 */
	public String[] getDrawText(String s) {
		String[] drawText = new String[PAGES];// 根据页数初始化数组
		for (int i = 0; i < PAGES; i++) {
			drawText[i] = ""; // 数组元素初始化为空字符串
		}
		int k, suffix = 0, lines = 0;
		while (s.length() > 0) {
			if (lines < 54) // 不够一页时
			{
				k = s.indexOf('\n');
				if (k != -1) // 存在回车符
				{
					lines += 1; // 行数累加
					// 计算该页的具体文本内容，存放到相应下标的数组元素
					drawText[suffix] = drawText[suffix] + s.substring(0, k + 1);
					if (s.substring(k + 1).length() > 0)
						s = s.substring(k + 1);
				} else {
					lines += 1; // 行数累加
					// 将文本内容存放到相应的数组元素
					drawText[suffix] = drawText[suffix] + s;
					s = "";
				}
			} else // 已满一页时
			{
				lines = 0; // 行数统计清零
				suffix++; // 数组下标加 1
			}
		}
		return drawText;
	}

	public int getPagesCount(String curStr) {
		int page = 0;
		int position, count = 0;
		String str = curStr;
		while (str.length() > 0) // 文本尚未计算完毕
		{
			position = str.indexOf('\n'); // 计算回车符的位置
			count += 1; // 统计行数
			if (position != -1)
				str = str.substring(position + 1); // 截取尚未计算的文本
			else
				str = ""; // 文本已计算完毕
		}
		if (count > 0)
			page = count / 54 + 1; // 以总行数除以 54 获取总页数
		return page; // 返回需打印的总页数
	}

	/**
	 * 计算
	 * 
	 * @param num
	 * @return
	 */
	public static int count(String[] num) {
		int count = 0;
		if (num != null && num.length > 0) {
			for (int i = 0; i < num.length; i++) {
				count += Integer.valueOf(num[i].substring(0, 1));
				// System.out.println(num[i].substring(0,1));
			}
		}
		return count;
	}

	/**
	 * 求和计算函数
	 * 
	 * @return
	 */
	public static double add(String[] sub) {
		double total = 0;
		if (sub != null && sub.length > 0) {
			for (int i = 0; i < sub.length; i++) {
				total += Double.valueOf(sub[i]);
			}
		}
		return total;
	}

	public static void main(String[] args) {

		System.out.println("开始打印...");
		String[] num = { "1", "2", "3", "4" };
		String[] dishes = { "aaa", "sfasf", "nnnn", "ccc" };
		String[] subtotal = { "12", "3", "2", "2" };
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		try {
			// num = ((String) arguments[0]).split(",");
			// dishes = ((String) arguments[1]).split(",");
			// subtotal = ((String) arguments[2]).split(",");

			sb.append("　　　　　兰州银行大食堂\n");

			sb.append("\n");
			// System.out.println("日期："+sdf.format(new Date()));
			sb.append("数量　　　　名称　　　　  小计\n");
			sb.append("------------------------\n");
			for (int i = 0; i < dishes.length; i++) {
				sb.append(num[i] + "　　　　" + dishes[i] + "　　　　　" + subtotal[i]
						+ "\n");
				sb.append("   \n");
			}
			sb.append("(数量总数：" + PrintUtil.count(num) + ")\n");
			sb.append("------------------------\n");
			sb.append("合计：" + PrintUtil.add(subtotal) + "\n");
			sb.append("------------------------\n");
			sb.append("\n");
			sb.append("打印时间：" + sdf.format(new Date()) + "\n");
			sb.append("非常感谢使用自助预定餐系统\n");
			sb.append("我们会为你提供更好的服务");

			LocatePrint lp = new LocatePrint(sb.toString());
			lp.printContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结束打印...");

	}

}