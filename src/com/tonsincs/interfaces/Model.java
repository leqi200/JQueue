package com.tonsincs.interfaces;

import java.nio.ByteBuffer;

/**
* @ProjectName:JQueue
* @ClassName: Model
* @Description: TODO(数据实体都要实现的一个模板)
* @author 萧达光
* @date 2014-5-28 下午01:37:59
* 
* @version V1.0 
*/
public interface Model {
	public int ParseTlvData(ByteBuffer pBuffer);
	public int EncodeTlvPacket(ByteBuffer pBuffer,short tag);
}
