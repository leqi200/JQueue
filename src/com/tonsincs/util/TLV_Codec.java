package com.tonsincs.util;

import java.nio.ByteBuffer;

/**
* @ProjectName:JQueue
* @ClassName: TLV_Codec
* @Description: TODO(TLV工具类，主要提供一些常用的操作方法)
* @author 萧达光
* @date 2014-5-28 下午01:33:18
* 
* @version V1.0 
*/
public class TLV_Codec {

	public static short DecodeTlvIntVal(ByteBuffer pBuf, short tag, int len) {

		return pBuf.getShort();
	}

	public static float DecodeTlvFloatVal(ByteBuffer pBuf, short tag, int len) {

		return pBuf.getFloat();
	}

	public static String DecodeTlvStringVal(ByteBuffer pBuf, short tag, int len) {

		byte[] data = new byte[len];
		for (int i = 0; i < len; i++) {
			data[i] = pBuf.get();
		}

		return new String(data);
	}

	public static byte[] DecodeTlvImageVal(ByteBuffer pBuf, short tag, int len) {

		return null;
	}

	public static byte DecodeTlvByteVal(ByteBuffer pBuf, short tag, int len) {
		// byte[] data=new byte[len];
		// for(int i=0;i<len;i++)
		// {
		// data[i]=pBuf.get();
		// }
		return 0;
	}

	public static int EncodeTlvIntVal(ByteBuffer pBuf, short tag, int val) {

		return 0;
	}

	public static int EncodeTlvFloatVal(ByteBuffer pBuf, short tag, float val) {

		return 0;
	}

	public static int EncodeTlvStringVal(ByteBuffer pBuf, short tag, String val) {

		return 0;
	}

	public static int EncodeTlvImageVal(ByteBuffer pBuf, short tag,
			byte[] data, int len) {

		return 0;
	}

	public static int EncodeTlvByteVal(ByteBuffer pBuf, short tag, Byte data,
			int len) {

		return 0;
	}

	public static short decodeTlvTag(ByteBuffer pBuf) {

		return pBuf.getShort();
	}

	public static short decodeTlvLen(ByteBuffer pBuf) {

		return pBuf.getShort();
	}

	public static short decodeHead(ByteBuffer pBuf) {

		return pBuf.getShort();
	}
}
