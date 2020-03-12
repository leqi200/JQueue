package com.tonsincs.util;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PrintSuper;
import com.tonsincs.entity.Print_Style_All;
import com.tonsincs.entity.Print_Style_NoTiicket;
import com.tonsincs.entity.Print_Style_Stock; 

/**
* @ProjectName:JQueue
* @ClassName: PrintFactory
* @Description: TODO(打印格式工厂类,根据不同的类型构造出一个打印对象)
* @author 萧达光
* @date 2014-5-28 下午01:30:06
* 
* @version V1.0 
*/
public class PrintFactory {

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:私有的构造函数;表明该工厂不能通过构造函数来创建
	 * </p>
	 */
	private PrintFactory() {
	}

	/**
	 * @Title: createTicket
	 * @Description: TODO(创建打印格式对象的工厂类)
	 * @param @param printType
	 * @param @return
	 * @return Ticket 返回类型
	 */
	public static PrintSuper createTicket(int printType, String printdata) {
		PrintSuper printsuper = null;
		switch (printType) {
		case Sys_Constant.ALL_PRINT:
			printsuper = new Print_Style_All(printdata);
			break;
		case Sys_Constant.STOCK_PRINT:
			printsuper = new Print_Style_Stock(printdata);
			break;

		case Sys_Constant.NOTICKET_PRINT:
			printsuper = new Print_Style_NoTiicket(printdata);
			break;

		default:
			break;
		}

		return printsuper;
	}

}
