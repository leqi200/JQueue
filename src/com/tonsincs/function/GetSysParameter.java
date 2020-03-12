package com.tonsincs.function;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.google.gson.Gson;
import com.tonsincs.entity.Parameter;
import com.tonsincs.entity.ParameterArray;
import com.tonsincs.util.SQLHelper;

/**
 * @ClassName: GetSysParameter
 * @Description: TODO(用于获取系统参数列表)
 * @author 萧达光
 * @date 2014-5-16 下午03:13:53
 * 
 * @version V1.0
 */
public class GetSysParameter extends BrowserFunction {
	private static Logger log = Logger.getLogger(GetSysParameter.class);

	public GetSysParameter(Browser browser, String name) {
		super(browser, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @Title: function
	 * @Description: TODO(获取系统所有参数)
	 * @param @param argument
	 * @param @return
	 * @return Object 返回类型
	 */
	@Override
	public Object function(Object[] arguments) {
		
		SQLHelper sqlHelper = new SQLHelper();
		List<Parameter> list=null;
		Gson gson=new Gson();
		List<Parameter> a=new ArrayList<Parameter>();
		List<Parameter> b=new ArrayList<Parameter>();
		List<Parameter> d=new ArrayList<Parameter>();
		List<Parameter> s=new ArrayList<Parameter>();
		//List<Parameter> p=new ArrayList<Parameter>();
		ParameterArray pa=null;
		try {
			list = sqlHelper.getSysParameter();
			for (Parameter par : list) {
				if (par.getType().equals("A")) {
					a.add(par);
				}else if (par.getType().equals("B")) {
					b.add(par);
				}else if (par.getType().equals("D")) {
					d.add(par);
				}else if (par.getType().equals("S")) {
					s.add(par);
				}
			}
			pa=new ParameterArray(a, b, d, s);
			log.info(gson.toJson(""));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("",e);
		}
		return gson.toJson(pa);
	}
}
