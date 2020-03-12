package com.tonsincs.function;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.main.JQ_Main;

/**
* @ProjectName:JQueue
* @ClassName: LoadMenu
* @Description: TODO(加载系统主界面菜单)
* @author 萧达光
* @date 2014-5-28 下午01:40:00
* 
* @version V1.0 
*/
public class LoadMenu extends BrowserFunction {
	private Browser browser;
	private static Logger log = Logger.getLogger(LoadMenu.class);

	public LoadMenu(Browser browser, String name) {
		super(browser, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @Title: function
	 * @Description: TODO(加载系统主界面菜单)
	 * @param @param argument
	 * @param @return[返回值的结构：]
	 * @return Object 返回类型
	 */
	@Override
	public Object function(Object[] arguments) {
		String rep = "";
		try {
			rep = JQ_Main.OS_CONTEXT.get("NO_PHONE_INPUT_OPTION_SERVER")
					+ Sys_Constant.DELIMITER
					+ JQ_Main.OS_CONTEXT.get("FORCE_NUMBER");
			log.info("加载系统主界面菜单:["+rep+"]");
		} catch (Exception e) {
			log.info("",e);
		}
		return rep;
	}

}
