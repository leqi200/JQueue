package com.tonsincs.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import com.tonsincs.function.Call;
import com.tonsincs.function.GetBizMenu;
import com.tonsincs.function.GetBizTip;
import com.tonsincs.function.GetSysParameter;
import com.tonsincs.function.GetTicketNumber;
import com.tonsincs.function.LoadMenu;
import com.tonsincs.function.Print;
import com.tonsincs.function.UpdateSysParameter;
import com.tonsincs.util.CommonSendLEDMsg;

/**
 * @ProjectName:JQueue
 * @ClassName: WebUI
 * @Description: TODO(WebUI插件封装类)
 * @author 萧达光
 * @date 2014-5-28 下午01:37:23
 * 
 * @version V1.0
 */
public class WebUI {

	private static WebUI INSTANCE = new WebUI();
	private Display display;
	private Shell shell;
	private Browser browser;
	private Rectangle displayRect;
	private String statusText;
	private Map<String, Object> context = new HashMap<String, Object>();
	private int x;
	private int y;
	private int width;
	private int height;

	// Socket 通讯基本参数
	private Socket socketClient = null; // Socket客户端（建立与服务器通讯用于发送命令通道）
	private DataInputStream dis = null; // 输入流
	private DataOutputStream dos = null; // 输出流
	private static final int BUFSIZE = 32;

	public static WebUI getInstance() {
		return INSTANCE;
	}

	private WebUI() {
		display = new Display();
		displayRect = display.getBounds();
		// 为Display绑定键盘侦听事件
		display.addFilter(SWT.KeyDown, new OS_KeyListener());
		shell = new Shell(display, SWT.NO_TRIM);
		shell.setText("排队机系统");
		shell.setLayout(null);

		browser = new Browser(shell, SWT.NONE);
		// 注册全局函数方便页面的调用
		new LoadMenu(browser, "loadMenu");// 加载系统菜单
		new GetBizMenu(browser, "getBizMenu");// 获取二级业务菜单
		new GetBizTip(browser, "getBizTip");// 取业务提示

		new GetTicketNumber(browser, "getTicketNumber");// 请求票号
		new GetSysParameter(browser, "getSysParameter");// 获取系统参数
		new UpdateSysParameter(browser, "sysupdate");// 更新系统参数函数
		new Call(browser, "call");// 提供一个测试呼叫的函数
		new Print(browser, "print");// 提供一个测试打印的函数
		 
	}

	private void _setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		if (x == Integer.MIN_VALUE && y == Integer.MIN_VALUE) {
			shell.setBounds((displayRect.width - width) >> 1,
					(displayRect.height - height) >> 1, width, height);
		} else {
			shell.setBounds(x, y, width, height);
		}
		browser.setBounds(0, 0, width, height);
	}

	/**
	 * 正常大小(一般与fullScreen配合使用)
	 */
	public void normalScreen() {
		_setBounds(x, y, width, height);
	} 

	/**
	 * 最小化窗体
	 */
	public void minScreen() {
		shell.setMinimized(true);
	}

	/**
	 * 窗体全屏
	 */
	public void fullScreen() {
		shell.setBounds(0, 0, displayRect.width, displayRect.height);
		browser.setBounds(0, 0, displayRect.width, displayRect.height);
	}

	/**
	 * 设置窗体宽高,自动居中
	 * 
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		_setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, width, height);
	}

	/**
	 * 设置窗体坐标和宽高
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds(int x, int y, int width, int height) {
		_setBounds(x, y, width, height);
	}

	/**
	 * 消息循环
	 */
	public void msgLoop() {
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent e) {
				statusText = e.text;
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		shell.dispose();
		display.dispose();
	}

	/**
	 * 窗体可见性
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		shell.setVisible(visible);
	}

	/**
	 * 跳转页面
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		browser.setUrl(url);
	}

	/**
	 * 置入HTML
	 * 
	 * @param html
	 */
	public void setHtml(String html) {
		browser.setText(html);
	}

	/**
	 * 取运行路径
	 * 
	 * @return
	 */
	public String getPath() {

		return "file:/" + System.getProperty("user.dir").replace("\\", "/");
	}

	/**
	 * 绑定事件
	 * 
	 * @param eventType
	 * @param listener
	 */
	public void bind(int eventType, Listener listener) {
		browser.addListener(eventType, listener);
	}

	/**
	 * 解绑事件
	 * 
	 * @param eventType
	 * @param listener
	 */
	public void unbind(int eventType, Listener listener) {
		browser.removeListener(eventType, listener);
	}

	/**
	 * 状态文本被改变事件
	 * 
	 * @param cb
	 */
	public void statusChanged(final Callback cb) {
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent e) {
				cb.handle(e);
			}
		});
	}
	
	

	/**
	 * 用于接收返回结果的消息
	 * 
	 * @return
	 */
	public String getMsg() {
		return (String) context.get("RESPOND_MSG");
	}

	/**
	 * 取状态文本
	 * 
	 * @return
	 */
	public String getStatus() {
		return statusText;
	}

	/**
	 * 执行脚本
	 * 
	 * @param script
	 * @return
	 */
	public boolean exec(String script) {
		return browser.execute(script);
	}

	/**
	 * 编译脚本
	 * 
	 * @param script
	 * @return
	 */
	public Object eval(String script) {
		return browser.evaluate(script);
	}

	/**
	 * 网页载入完毕事件
	 * 
	 * @param cb
	 */
	public void completed(final Callback cb) {
		browser.addProgressListener(new ProgressListener() {
			public void completed(ProgressEvent e) {
				cb.handle();

			}

			public void changed(ProgressEvent e) {
				 
			}
		});
	}

	/**
	 * 为Display绑定键盘事件处理回调类
	 */
	private class OS_KeyListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			// TODO Auto-generated method stub
			// System.out.println(event);
			if (((event.stateMask & SWT.ALT)) == SWT.ALT
					&& (event.keyCode == 16777229)) {// 退出系统
				// if (socketClient != null) {
				// try {
				// socketClient.close();
				// } catch (IOException e) {
				//
				// }
				// socketClient = null;
				// }
				// 释放系统占用的资源
				JQ_Main.RUN_TEMPORARY.clear();
				JQ_Main.OS_CONTEXT.clear();
				JQ_Main.OS_CONTEXT = null;
				JQ_Main.RUN_TEMPORARY = null;

				System.exit(0);
			}
		}

	}

	/**
	 * 监听鼠标点击事件
	 */
	public void mouseListener() {
		browser.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * @功能 回调函数
	 * 
	 */
	public class Callback {

		public void handle() {
		}

		public void handle(Object o) {
		}

	}
}
