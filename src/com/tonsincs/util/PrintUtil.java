package com.tonsincs.util;
 
import java.awt.Color; 
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D; 
import java.awt.RenderingHints; 
import java.awt.font.FontRenderContext; 
import java.awt.print.PageFormat; 
import java.awt.print.Printable;
import java.awt.print.PrinterException; 
import java.text.SimpleDateFormat; 

/**
* @ProjectName:JQueue
* @ClassName: PrintUtil
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 萧达光
* @date 2014-5-28 下午01:30:24
* 
* @version V1.0 
*/
public class PrintUtil implements Printable {

	private String printText;
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private  String[] num;
	private  String[] dishes;
	private  double[] subtotal;
	private  int PAGES=0;
	
	private double paperW=0;//打印的纸张宽度 

	private double paperH=0;//打印的纸张高度 

	public PrintUtil(String printText){
		super();
		this.printText=printText;
	}
	public PrintUtil(String printText,double paperW,double paperH) {
		super();
		this.printText = printText;
		this.PAGES=getPagesCount(printText);
		this.paperW=paperW;
		this.paperH=paperH;
	}

	public PrintUtil(String printText,String[] num,String[] dishes,double[] subtotal) {
		super();
		this.printText = printText;
		this.PAGES=getPagesCount(printText);
		this.num=num;
		this.dishes=dishes;
		this.subtotal=subtotal;
	}
	/*
	 * Graphic 指明打印的图形环境；PageFormat 指明打印页格式（页面大小以点为计量单位， 1 点为 1 英才的 1/72，1 英寸为
	 * 25.4 毫米。A4 纸大致为 595 × 842 点）；page 指明页号
	 */
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black); // 设置打印颜色为黑色
		if (page >= PAGES){
			// 当打印页号大于需要打印的总页数时，打印工作结束
			return Printable.NO_SUCH_PAGE;
		}
		// Paper paper = pf.getPaper();//得到页面格式的纸张 paper.setSize(500,500);//纸张大小
		// paper.setImageableArea(0,0,paperW,paperH); //设置打印区域的大小
		// paper.setSize(paperW, paperH);
		// pf.setPaper(paper);//将该纸张作为格式
		drawCurrentPageText(g2, pf, page); // 打印当前页文本
		return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
	}

	/* 打印指定页号的具体文本内容 */
	private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {
		String s = getDrawText(printText)[page];// 获取当前页的待打印文本内容
		
		// 获取默认字体及相应的尺寸
		FontRenderContext context = g2.getFontRenderContext();
		Font f = new Font("新宋体", Font.PLAIN, 9);
		 g2.setFont(f);// 设置字体
		 
		// 使用抗锯齿模式完成文本呈现
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
		// 打印起点坐标
		double x = pf.getImageableX();
		double y = pf.getImageableY();
		float heigth = f.getSize2D();// 字体高度
		String drawText;
		float ascent = 9; // 给定字符点阵
		int k, i = f.getSize(), lines = 0;
		while (s.length() > 0 && lines < 54) // 每页限定在 54 行以内
		{
			k = s.indexOf('\n'); // 获取每一个回车符的位置
			if (k != -1) // 存在回车符
			{
				lines += 1; // 计算行数
				drawText = s.substring(0, k); // 获取每一行文本
				g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
				//g2.drawString(drawText, 0, (float) y + 1 * heigth );
				//g2.drawString(drawText, 200, 200);
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
	}

	/* 将打印目标文本按页存放为字符串数组 */
	public String[] getDrawText(String s) {
		String[] drawText = new String[PAGES];// 根据页数初始化数组
		for (int i = 0; i < PAGES; i++){
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
				total +=Double.valueOf(sub[i]);
			}
		}
		return total;
	}

}
