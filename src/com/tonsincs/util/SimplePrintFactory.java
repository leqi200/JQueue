package com.tonsincs.util;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.AllPrintInfo;
import com.tonsincs.entity.NoTicketPrintInfo;
import com.tonsincs.entity.StockPrintInfo;
import com.tonsincs.entity.Ticket;

/**
* @ProjectName:JQueue
* @ClassName: SimplePrintFactory
* @Description: TODO(打印格式工厂类,根据不同的类型构造出一个打印对象)
* @author 萧达光
* @date 2014-5-28 下午01:32:04
* 
* @version V1.0 
*/
public class SimplePrintFactory {

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:私有的构造函数;表明该工厂不能通过构造函数来创建
	 * </p>
	 */
	private SimplePrintFactory() {
	}

	/**
	 * @Title: createTicket
	 * @Description: TODO(创建打印格式对象的工厂类)
	 * @param @param printType
	 * @param @return
	 * @return Ticket 返回类型
	 */
	public static Ticket createTicket(int printType,String printdata) {
		Ticket ticket = null;
		switch (printType) {
		case Sys_Constant.ALL_PRINT:
			ticket = new AllPrintInfo(printdata);
			break;
		case Sys_Constant.STOCK_PRINT:
			ticket = new StockPrintInfo(printdata);
			break;

		case Sys_Constant.NOTICKET_PRINT:
			ticket = new NoTicketPrintInfo(printdata);
			break;

		default:
			break;
		}

		return ticket;
	}

}
