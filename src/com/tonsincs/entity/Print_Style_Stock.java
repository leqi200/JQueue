package com.tonsincs.entity;

import java.io.IOException;

import com.google.gson.Gson;
import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.main.JQ_Main;
import com.tonsincs.util.TonsincsSerialPortsPrint;

/**
* @ProjectName:JQueue
* @ClassName: Print_Style_Stock
* @Description: TODO(最常用的打印票号格式)
* @author 萧达光
* @date 2014-5-28 下午01:43:13
* 
* @version V1.0 
*/
public class Print_Style_Stock extends PrintSuper {

	public Print_Style_Stock(String printStr) {
		super(printStr);
		String json = JQ_Main.OS_CONTEXT.get(Sys_Constant.PRINT_ALL_STYLE);
		Gson gson = new Gson();
		this.pp = gson.fromJson(json, PrintParameter.class);
		this.printdata=printStr.split(Sys_Constant.DELIMITER);
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
		//this.phone = printdata[11];// 手机号码
	}
	/**
	* @Title: print
	* @Description: TODO(实现具体的打印功能)
	* @param  
	* @return void    返回类型
	*/
	@Override
	public void print() {
		String com=JQ_Main.OS_CONTEXT.get("PRINTER_COM");
		int baudRate=Integer.parseInt(JQ_Main.OS_CONTEXT.get("PRINTER_BAUD_RATE"));
		TonsincsSerialPortsPrint tspp=new TonsincsSerialPortsPrint(com, baudRate);
		String comp=JQ_Main.OS_CONTEXT.get("COMPANY_TITLE");
		try {
			
			tspp.tonsinsPrnterSetFont((byte)pp.company.getW(),(byte) pp.company.getH());
			tspp.tonsinsPrnterPrintLine(comp+company);//打印公司全称名称
			tspp.tonsinsPrnterSetFont((byte)pp.sellingArea.getW(),(byte) pp.sellingArea.getH());
			tspp.tonsinsPrnterPrintLine(sellingArea);//打印服务厅名称
			tspp.tonsinsPrnterLF(); 
			tspp.tonsinsPrnterSetFont((byte)pp.bizname.getW(),(byte) pp.bizname.getH());
			tspp.tonsinsPrnterPrintLine(bizname);//打印业务名称
			tspp.tonsinsPrnterSetFont((byte)pp.ticketNo.getW(),(byte) pp.ticketNo.getH());
			tspp.tonsinsPrnterPrintLine(ticketNo);//打印排队号码
			tspp.tonsinsPrnterLF(); 
			tspp.tonsinsPrnterSetFont((byte)pp.counter.getW(),(byte) pp.counter.getH());
			tspp.tonsinsPrnterPrintLine(counter);//打印窗口号
			
			tspp.tonsinsPrnterSetFont((byte)pp.waitNum.getW(),(byte) pp.waitNum.getH());
			tspp.tonsinsPrnterPrintLine("前面等待人数:"+waitNum);//打印等待人数
			tspp.tonsinsPrnterSetFont((byte)1,(byte) 2);
			tspp.tonsinsPrnterPrintLine("________________________________________________");//打印划线
			tspp.tonsinsPrnterLF(); 
			String[] printContens=splitting(content, Sys_Constant.PRINT_LINE_NUM);
			//打印内容，并且智能换行
			for (int i = 0; i < printContens.length; i++) {
				tspp.tonsinsPrnterSetFont((byte)pp.content.getW(),(byte) pp.content.getH());
				tspp.tonsinsPrnterPrintLine(printContens[i]);//打印补充内容
			}
			//打印分割线
			//tspp.tonsinsPrnterSetFont((byte)1,(byte) 1);
			//tspp.tonsinsPrnterPrintLine("------------------------------------------------");//打印划线
			//打印主动营销信息
			// printContens=splitting(marketing, Sys_Constant.PRINT_LINE_NUM);
			// for (int i = 0; i < printContens.length; i++) {
			// tspp.tonsinsPrnterSetFont((byte)pp.marketing.getW(),(byte)
			// pp.marketing.getH());
			// tspp.tonsinsPrnterPrintLine(printContens[i]);//打印补充内容
			// }
			tspp.tonsinsPrnterSetFont((byte)1,(byte) 2);
			tspp.tonsinsPrnterPrintLine("________________________________________________");//打印划线
			tspp.tonsinsPrnterLF(); 
			//打印取号时间
			tspp.tonsinsPrnterSetFont((byte)pp.ticketTime.getW(),(byte) pp.ticketTime.getH());
			tspp.tonsinsPrnterPrintLine("取号时间："+ticketTime);
			 
			//打印取票手机
			tspp.tonsinsPrnterSetFont((byte)pp.phone.getW(),(byte) pp.phone.getH());
			tspp.tonsinsPrnterPrintLine(phone);//打印公司名称
			//走纸
			tspp.tonsinsPrnterLF();
			tspp.tonsinsPrnterLF();
			tspp.tonsinsPrnterCutPaper();
			tspp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}		

	}
	// public static void main(String[] args) {
	// String str =
	// "AAGZ00001~广州分公司~中信营业厅~A~综合业务~X02~5~受理窗口:1、2、3~2014-03-19 16:20:30~你可以登录门户网站(http://www.gdchinamobile.com/gx/)的优惠资讯栏目，随时随地查询各类优惠活动的资讯。~主动营销内容:.............~取票手机:1392103233,请您凭号服务";
	// PrintSuper psa = new Print_Style_Stock(str);
	// psa.print();
	// System.out.println(psa.pp);
	// }

}
