package com.tonsincs.util;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList; 
import java.util.List; 

import com.tonsincs.entity.Param;
import com.tonsincs.entity.Parameter;

/**
* @ProjectName:JQueue
* @ClassName: SQLHelper
* @Description: TODO(数据库操作工具类)
* @author 萧达光
* @date 2014-5-28 下午01:32:41
* 
* @version V1.0 
*/
public class SQLHelper {

	/**
	 * 数据查询
	 * 
	 * @param sql语句
	 * @return 返回结果集List<Object>
	 * @throws SQLException
	 */
	public List<Object> query(String sql) throws SQLException {
		if (sql.equals("") || sql == null) {
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = C3P0ConnentionProvider.getConnection();
		ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		// 可以得到有多少列
		int columnNum = rsmd.getColumnCount();
		// 将数据封装到list中
		while (rs.next()) {
			Object[] objects = new Object[columnNum];
			for (int i = 0; i < objects.length; i++) {
				objects[i] = rs.getObject(i + 1);
			}
			list.add(objects);
		}

		return list;
	}

	/**
	 * @Method: query
	 * @Description: 公共 数据查询函数
	 * @param @param sql 执行查询语句
	 * @param @param values 参数值
	 * @param @return
	 * @param @throws SQLException
	 * @return List<Object>
	 * @throws
	 */
	@SuppressWarnings("unused")
	public List<Object> query(String sql, Object[] values) throws SQLException {
		if (sql.equals("") || sql == null) {
			return null;
		}

		List<Object> list = new ArrayList<Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// 创建查询
			conn = C3P0ConnentionProvider.getConnection();
			ps = conn.prepareStatement(sql);
			if (values == null && values == null) {
				rs = ps.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				// 可以得到有多少列
				int columnNum = rsmd.getColumnCount();
				while (rs.next()) {
					Object[] objects = new Object[columnNum];
					for (int i = 0; i < objects.length; i++) {
						objects[i] = rs.getObject(i + 1);
					}
					list.add(objects);
				}
			} else {
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
				rs = ps.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				// 可以得到有多少列
				int columnNum = rsmd.getColumnCount();
				while (rs.next()) {
					Object[] objects = new Object[columnNum];
					for (int i = 0; i < objects.length; i++) {
						objects[i] = rs.getObject(i + 1);
					}
					list.add(objects);
				}
			}
		} finally {
			// TODO: handle exception
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return list;
	}

	/**
	 * 插入、修改数据操作
	 * 
	 * @param sql语句
	 * @return boolean 成功返回true，失败返回false
	 * @throws SQLException
	 */
	public boolean update(String sql) throws SQLException {
		boolean b = false;
		if (sql.equals("") || sql == null) {
			return b;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = C3P0ConnentionProvider.getConnection();
			ps = conn.prepareStatement(sql);
			int i = ps.executeUpdate();
			if (i == 1) {
				b = true;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return b;
	}

	/**
	 * @Method: update
	 * @Description: TODO
	 * @param @param sql 要执行的插入、更新操作SQL语句
	 * @param @param values 参数值
	 * @param @return
	 * @param @throws SQLException
	 */
	public boolean update(String sql, Object[] values) throws SQLException {
		boolean b = false;
		if (sql.equals("") || sql == null) {
			return b;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = C3P0ConnentionProvider.getConnection();
			ps = conn.prepareStatement(sql);
			if (values != null && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
			}
			int i = ps.executeUpdate();
			if (i == 1) {
				b = true;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return b;
	}

	/**
	 * @Title: batchUpdate
	 * @Description: TODO(批量执行系统参数更新,带事务)
	 * @param @param sql 更新SQL语句
	 * @param @param key
	 * @param @param values
	 * @return boolean 返回类型
	 * @throws SQLException
	 */
	public boolean batchUpdate(Object[] key, Object[] values) {
		boolean b = false;
		PreparedStatement ps = null;
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

			conn = C3P0ConnentionProvider.getConnection();
			conn.setAutoCommit(false); // 关闭自动提交

			// st = conn.createStatement();
			// ps=conn.prepareStatement("UPDATE `parameter` SET `q_values`=? where `q_keys`=?");

			ps = conn
					.prepareStatement("UPDATE parameter SET q_values=? where q_keys=?");
			for (int i = 0; i < values.length; i++) {
				ps.setObject(1, values[i]);
				ps.setObject(2, key[i]);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			b=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	* @Title: batchUpdate
	* @Description: TODO(批量执行系统参数更新,带事务)
	* @param @param params
	* @param @return 
	* @return boolean    返回类型
	*/
	public boolean batchUpdate(List<Param> params){
		boolean b = false;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = C3P0ConnentionProvider.getConnection();
			conn.setAutoCommit(false); // 关闭自动提交
			ps = conn
					.prepareStatement("UPDATE parameter SET q_values=? where q_keys=?");
			for (Param param : params) {
				ps.setObject(1, param.value);
				ps.setObject(2, param.name);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			b=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	/**
	 * @Title: getSysParameter
	 * @Description: TODO(获取系统参数列表,目前这一个函数定义在这里严格来说是不好的，以后会迁移到其它类中)
	 * @param @return
	 * @return List<Parameter> 返回类型
	 */
	public List<Parameter> getSysParameter() throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		List<Parameter> list = new ArrayList<Parameter>();
		try {
			conn = C3P0ConnentionProvider.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM parameter");
			while (rs.next()) {
				Parameter par = new Parameter(rs.getInt(1), rs.getString(2), rs
						.getString(3), rs.getString(4),rs.getString(5),rs.getString(6));
				list.add(par);
			}
		} finally {
			// TODO: handle exception
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return list;
	}

	public static void main(String[] args) {
		SQLHelper sqlHelper = new SQLHelper();
		// new String[]{"话费缴纳","综合业务1"}
		// sqlHelper.batchUpdate(new String[]{"B","A"},new String[]{"bb","aa"});
	}
}
