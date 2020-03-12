package com.tonsincs.function;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.Param;
import com.tonsincs.util.SQLHelper;

/**
* @ProjectName:JQueue
* @ClassName: UpdateSysParameter
* @Description: TODO(更新系统配置参数函数)
* @author 萧达光
* @date 2014-5-28 下午01:40:45
* 
* @version V1.0 
*/
public class UpdateSysParameter extends BrowserFunction {
	private static Logger log=Logger.getLogger(UpdateSysParameter.class);
	public UpdateSysParameter(Browser browser, String name) {
		super(browser, name);
	}

	/**
	 * @Title: function
	 * @Description: TODO(更新系统参数配置函数)
	 * @param @param argument
	 * @param @return
	 * @return Object 返回类型
	 */
	@Override
	public Object function(Object[] arguments) {
		int flag=0;
		boolean b = false;
		try {
			Object[] aa = arguments;
			System.out.println(arguments[0]);
			String json = (String) arguments[0];
			Gson gson = new Gson();
			// List<Param> pp=(List<Param>) gson.fromJson(json,Param.class);
			Type type = new TypeToken<List<Param>>() {
			}.getType();
			List<Param> list = gson.fromJson(json, type);
			//执行更新操作
			SQLHelper sqlHelper=new SQLHelper();
		    b= sqlHelper.batchUpdate(list);
			flag=b==true?Sys_Constant.SUCCESS:Sys_Constant.FAIL;
			// System.out.println();
			// System.out.println(gson.toString());
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("",e);
		}
		return flag;
	}

}
