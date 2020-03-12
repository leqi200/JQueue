package com.tonsincs.entity;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import com.tonsincs.constant.Sys_Constant;

/**
* @ProjectName:JQueue
* @ClassName: StockPrintInfo
* @Description: TODO(这最常用票号打印格式,不带主动营销信息)
* @author 萧达光
* @date 2014-5-28 下午01:43:56
* 
* @version V1.0 
*/
public final class StockPrintInfo extends Ticket {

	public StockPrintInfo(String printdata) {
		// TODO Auto-generated constructor stub
		super(printdata);
		if (printStr != null) {
			// 以分割符为"~",分割字符串
			this.printdata = printStr.split(Sys_Constant.DELIMITER);
		}
	}

	@Override
	public int print(Graphics arg0, PageFormat arg1, int arg2)
			throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}
}
