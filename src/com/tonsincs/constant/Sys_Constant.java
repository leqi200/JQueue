package com.tonsincs.constant;

/**
* @ProjectName:JQueue
* @ClassName: Sys_Constant
* @Description: TODO(系统常量定义类)
* @author 萧达光
* @date 2014-5-28 下午01:44:43
* 
* @version V1.0 
*/
public class Sys_Constant {

	/**
	 *读卡失败
	 */
	public static final int CR_FAIL = 101;
	/**
	 *读卡超时
	 */
	public static final int CR_OVERTIME = 102;
	/**
	 *函数参数为空
	 */
	public static final int CR_EMPTY_PARAMETERS = 103;
	/**
	 *回调函数执行失败(未知异常状态)
	 */
	public static final int CR_CALLBACK_FAIL = 104;
	/**
	* @Fields FAIL : TODO(表示执行失败)
	*/
	public static final int FAIL = -1;
	/**
	* @Fields SUCESS : TODO(用于表示执行成功)
	*/
	public static final int SUCCESS = 0;
	
	/**
	* @Fields PRINT_ALL_STYLE : TODO(打印所有信息票号样式)
	*/
	public static final String PRINT_ALL_STYLE="P1";
	/**
	* @Fields PRINT_STOCK_STYLE : TODO(打印最常用的票号样式)
	*/
	public static final String PRINT_STOCK_STYLE="P2";
	/**
	* @Fields PRINT_NOTICKET_STYLE : TODO(打印没有带票号的样式)
	*/
	public static final String PRINT_NOTICKET_STYLE="P3";
	
	/**
	* @Fields PRINT_LINE_NUM : TODO(表示每一行打印多少个字)
	*/
	public static final int PRINT_LINE_NUM=20;
	/*
	 * =========================
	 * 与广州移动排管系统对接的指令参数定义时间：2014年4月24号版本：v1.0.1
	 * =========================
	 */

	/**
	 * @Fields GET_PARAMETER : TODO(取参数设置指令)
	 */
	public static final int GET_PARAMETER = 0x00000201;
	/**
	 * @Fields GET_PARAMETER_REPLY : TODO(取参数设置响应指令)
	 */
	public static final int GET_PARAMETER_REPLY = 0x80000201;

	/**
	 * @Fields GET_BIZ_MENU : TODO(请求业务选择菜单指令)
	 */
	public static final int GET_BIZ_MENU = 0x00000202;
	/**
	 * @Fields GET_BIZ_MENU_REPLY : TODO(请求业务选择菜单响应指令)
	 */
	public static final int GET_BIZ_MENU_REPLY = 0x80000202;
	/**
	 * @Fields GET_TICKET_NO : TODO(请求票号指令)
	 */
	public static final int GET_TICKET_NO = 0x00000203;
	/**
	 * @Fields GET_TICKET_NO_REPLY : TODO(请求票号响应指令)
	 */
	public static final int GET_TICKET_NO_REPLY = 0x80000203;

	/**
	 * @Fields COUNTER_CALL : TODO(窗口叫号指令)
	 */
	public static final int COUNTER_CALL = 0x00000204;
	/**
	 * @Fields COUNTER_CALL_REPLY : TODO(窗口叫号指令响应码)
	 */
	public static final int COUNTER_CALL_REPLY = 0x80000204;

	/**
	 * @Fields COUNTER_CALL : TODO(窗口暂停叫号响应指令)
	 */
	public static final int COUNTER_PAUSE = 0x00000205;
	/**
	 * @Fields COUNTER_PAUSE_REPLY : TODO(窗口暂停叫号响应指令)
	 */
	public static final int COUNTER_PAUSE_REPLY = 0x80000205;

	/**
	 * @Fields ON_OFF : TODO(关机/重启指令)
	 */
	public static final int ON_OFF = 0x00000206;
	/**
	 * @Fields ON_OFF_REPLY : TODO(关机/重启指令响应指令)
	 */
	public static final int ON_OFF_REPLY = 0x80000206;
	/**
	 * @Fields CONFIGURE : TODO(设置排管系统服务器参数指令)
	 */
	public static final int GET_SERVER_CONFIGURE = 0x00000207;
	/**
	 * @Fields CONFIGURE_PEPLY : TODO(设置排管系统服务器参数响应指令)
	 */
	public static final int GET_SERVER_CONFIGURE_PEPLY = 0x80000207;
	/**
	 * @Fields GET_SERVICE_HINT : TODO(取业务提示指令)
	 */
	public static final int GET_SERVICE_HINT = 0x00000208;
	/**
	 * @Fields GET_SERVICE_HINT_PEPLY : TODO(取业务提示响应指令)
	 */
	public static final int GET_SERVICE_HINT_PEPLY = 0x80000208;
	//----------------------------------------------------------------------------
	//Socket 通讯一些参数默认配置
	
	/**
	 * @Fields SOCKET_OUT_TIME : TODO(排管系统服务端口)
	 */
	public static final int SERVEL_PORT=8500;
	/**
	 * @Fields SOCKET_OUT_TIME : TODO(排队机服务默认本地端口)
	 */
	public static final int LOCA_PORT=9001;
	
	/**
	* @Fields SOCKET_OUT_TIME : TODO(Socket 超时时间)
	*/
	public static final int SOCKET_OUT_TIME=10000;
	
	/**
	* @Fields SOCKET_TASK_OUT_TIME : TODO(Socket 任务调度超时时间)
	*/
	public static final int SOCKET_TASK_OUT_TIME=10000;
	
	/**
	 * @Fields DELIMITER : TODO(用于作为数据体分割的符号)
	 */
	public static final String DELIMITER = "~";

	/**
	 * @Fields ALL_PRINT : TODO(包含所有信息打印)
	 */
	public static final int ALL_PRINT = 1;
	/**
	 * @Fields STOCK_PRINT : TODO(最常用格式，不带主动营销信息)
	 */
	public static final int STOCK_PRINT = 2;
	/**
	 * @Fields NOTICKET_PRINT : TODO(不出票号格式)
	 */
	public static final int NOTICKET_PRINT = 3;
	
	//---------------------------------------------------------------
	//排队机系统一些硬件上的配置参数常量
	/**
	* @Fields SYS_DEFAULT_ENCODED : TODO(系统默认编码格式)
	*/
	public static final String SYS_DEFAULT_ENCODED="GBK";//"UTF-8";
	/**
	* @Fields SYS_TIME_DELAY : TODO(系统统一使用延时时间使用的毫秒数)
	*/
	public static final long SYS_TIME_DELAY=5000;
	
	/**
	* @Fields SYS_DEFALUT_BAUDRATE : TODO(系统默认波特率)
	*/
	public static final int SYS_DEFALUT_BAUDRATE=19200;
	
	
	//----------------------------------------------------------------
	//LED控制编码
	/**
	* @Fields LED_A_AREA : TODO(　A区显示（区域控制码）这个控制码必须在显示数据包的第一字节)
	*/
	public static final int LED_A_AREA=0x01;
	/**
	* @Fields LED_B_AREA : TODO(　B 区显示（区域控制码）这个控制码必须在显示数据包的第一字节)
	*/
	public static final int LED_B_AREA=0x02;
	/**
	* @Fields LED_FONT_16_MATRIX : TODO(　16点阵（字体控制码）)
	*/
	public static final int LED_FONT_16_MATRIX=0x03;
	/**
	* @Fields LED_FONT_24_MATRIX : TODO(　24点阵（字体控制码）)
	*/
	public static final int LED_FONT_24_MATRIX=0x04;
	/**
	* @Fields LED_FONT_32_MATRIX : TODO(　32点阵（字体控制码）)
	*/
	public static final int LED_FONT_32_MATRIX=0x05;
	/**
	* @Fields LED_FONT_RED : TODO(　红色显示（颜色控制码）)
	*/
	public static final int LED_FONT_RED=0x06;
	/**
	* @Fields LED_FONT_GREEN : TODO(　绿色显示（颜色控制码）)
	*/
	public static final int LED_FONT_GREEN=0x07;
	/**
	* @Fields LED_FONT_ORANGE : TODO(　橙色显示（颜色控制码）)
	*/
	public static final int LED_FONT_ORANGE=0x08;
	/**
	* @Fields LED_FONT_16X16_MATRIX : TODO(　显示16x16点阵窗口号  必须跟在区域控制码后面（如果要显示的话）)
	*/
	public static final int LED_FONT_16X16_MATRIX=0x09;
	/**
	* @Fields LED_FONT_32X32_MATRIX : TODO(　显示32x32点阵窗口号  必须跟在区域控制码后面（如果要显示的话）)
	*/
	public static final int LED_FONT_32X32_MATRIX=0x0A; 
	
	public static final int LED_SEND_0509_BROADCAST_ADDRESS=0x00;

}
