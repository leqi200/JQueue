package com.tonsincs.entity;

/**
* @ProjectName:JQueue
* @ClassName: PrintItem
* @Description: TODO(打印票号打印项的定义)
* @author 萧达光
* @date 2014-5-28 下午01:43:28
* 
* @version V1.0 
*/
public class PrintItem {
	private String title;
	private String val;
	private int w;
	private int h;
	private int fontsize;
	private int x;
	private int y;

	public PrintItem() {
		super();
	}
	

	public PrintItem(String title, String val, int w, int h, int fontsize,
			int x, int y) {
		super();
		this.title = title;
		this.val = val;
		this.w = w;
		this.h = h;
		this.fontsize = fontsize;
		this.x = x;
		this.y = y;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public int getFontsize() {
		return fontsize;
	}

	
	public int getW() {
		return w;
	}


	public void setW(int w) {
		this.w = w;
	}


	public int getH() {
		return h;
	}


	public void setH(int h) {
		this.h = h;
	}


	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
