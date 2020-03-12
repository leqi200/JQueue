package com.tonsincs.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.main.JQ_Main;

/**
* @ProjectName:JQueue
* @ClassName: AllPrintInfo
* @Description: TODO(包含所有打印信息打印对象)
* @author 萧达光
* @date 2014-5-28 下午01:41:08
* 
* @version V1.0 
*/
public final class AllPrintInfo extends Ticket {

	public AllPrintInfo(String printStr) {
		super(printStr);
		if (printStr != null) {
			// 以分割符为"~",分割字符串
			printdata = printStr.split(Sys_Constant.DELIMITER);
			this.channelNo = printdata[0];// 渠道编号
			this.company = printdata[1]; // 公司名称
			this.sellingArea = printdata[2];// 服务厅名称
			this.bizNo = printdata[3]; // 业务编码
			this.bizname = printdata[4];// 业务名称
			this.ticketNo = printdata[5];// 排队号码
			this.waitNum = printdata[6];// 等候人数
			this.counter = printdata[7];// 窗口号
			this.ticketTime = printdata[8];// 取号时间
			this.content = printdata[9];// 补充内容
			this.marketing = printdata[10];// 营销内容
			this.phone = printdata[11];// 手机号码
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
		int y1 = 1;
		g2.translate(x, y);// 转换坐标，确定打印边界
		// 开始打印公司名称标题
		// 具体打印每一行文本，同时走纸移位
		g2.setFont(fon_8);
		g2.drawString(JQ_Main.OS_CONTEXT.get("COMPANY_TITLE") + this.company,
				(float) x + 10, (float) y + 1 * heigthl_8);
		// 打印营业厅
		g2.drawString(this.sellingArea, (float) x + 10, (float) y + 2 * heigthl_8);
		// 打印业务名称
		g2.setFont(fon_14);// 设置14号字体
		g2.drawString(this.bizname, (float) x + 5, (float) y + 3 * heigthl_14);
		// 受理窗口
		g2.setFont(fon_9);
		g2.drawString(this.counter, (float) x + 100, (float) y + 3
						* heigthl_14);
		// 打印票号
		g2.setFont(fon_14);
		g2.drawString(this.ticketNo, (float) x + 5, (float) y + 4 * heigthl_14);
		// 等待人数
		g2.setFont(fon_9);
		g2.drawString("前面等待人数:" + this.waitNum, (float) x + 100, (float) y + 4
				* heigthl_14);
		// 打印下划线
		g2.drawLine((int) x - 2, (int) (y + ((float) 6.0 + y1) * heigthl_9),
				(int) x + 200, (int) (y + ((float) 6.0 + y1) * heigthl_9));
		
		return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
	}

	public static void main(String[] args) {
		String str = "AAGZ00001~中国移动广州分公司~中信营业厅~A~综合业务~X02~5~受理窗口:1、2、3~2014-03-19 16:20:30~当日有效,请留意叫号..............~移动新业务:.............~取票手机:1392103233,请您凭号服务";
		AllPrintInfo apf = new AllPrintInfo(str);
		apf.printContent();
		
	}

}
