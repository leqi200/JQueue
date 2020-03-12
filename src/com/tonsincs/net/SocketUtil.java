package com.tonsincs.net;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.tonsincs.constant.Sys_Constant;
import com.tonsincs.entity.PG_Package;

/**
* @ProjectName:JQueue
* @ClassName: SocketUtil
* @Description: TODO(这个是用于Socket通讯封装好的一些工具类)
* @author 萧达光
* @date 2014-5-28 下午01:26:00
* 
* @version V1.0 
*/
public class SocketUtil {

	/**
	* @Title: wrapup
	* @Description: TODO(将指定的字节数组包裹成PG_Package返回)
	* @param @param data字节数组
	* @param @param len包的总长度
	* @param @return 返回封装好的PG_Package包
	* @return PG_Package    返回类型
	 * @throws UnsupportedEncodingException 
	*/
	public static PG_Package wrapup(byte[] data,int len) throws UnsupportedEncodingException{
		PG_Package pack=null;
		if (len>0) {
			//初始化
			ByteBuffer buff = ByteBuffer.allocate(len);
			buff.put(data);
			buff.flip();//重置指针
			pack=new PG_Package();
			pack.setPktLength(len);
			pack.setCmdID(buff.getInt());
			pack.setCmdStatus(buff.getInt());
			pack.setSerialNo(buff.getInt());
			pack.setReserverd(buff.getInt());
			byte[] body_byte = new byte[buff.capacity() - buff.position()];
			buff.get(body_byte);
			String body=new String(body_byte,
					Sys_Constant.SYS_DEFAULT_ENCODED);
			pack.setBody(body.substring(0,body.length()-1));
			pack.setBuf(data);

		}
		return pack;
	}
}
