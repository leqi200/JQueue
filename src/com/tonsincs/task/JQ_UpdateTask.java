package com.tonsincs.task;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.Map.Entry;

import com.tonsincs.main.JQ_Main;
import com.tonsincs.util.SQLHelper;

/**
* @ProjectName:JQueue
* @ClassName: JQ_UpdateTask
* @Description: TODO(定义时执行一些系统参数更新的操作)
* @author 萧达光
* @date 2014-5-28 下午01:26:38
* 
* @version V1.0 
*/
@Deprecated
public class JQ_UpdateTask extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!JQ_Main.RUN_TEMPORARY.isEmpty()
				&& JQ_Main.RUN_TEMPORARY.size() > 0) {
			String sql = "";
			SQLHelper sqlHelper = new SQLHelper();
			//获取要更新的数据
			Iterator ite = JQ_Main.RUN_TEMPORARY.entrySet().iterator();
			try {
				while (ite.hasNext()) {
					Map.Entry<String, String> entry = (Entry<String, String>) ite
							.next();
					sql = "UPDATE parameter SET value=" + entry.getValue()
							+ " WHERE key=" + entry.getKey();
					//执行SQL语句,更新数据库
					sqlHelper.update(sql);
					JQ_Main.RUN_TEMPORARY.remove(entry.getKey());//清除修改过的值
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
