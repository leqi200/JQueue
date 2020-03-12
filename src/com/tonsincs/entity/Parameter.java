package com.tonsincs.entity;

/**
* @ProjectName:JQueue
* @ClassName: Parameter
* @Description: TODO(系统参数实体)
* @author 萧达光
* @date 2014-5-28 下午01:41:49
* 
* @version V1.0 
*/
public class Parameter {
	private Integer id;
	private String key;
	private String value;
	private String describe;
	private String type;
	private String title;

	public Parameter() {
		super();
	}

	public Parameter(Integer id, String key, String value, String describe,
			String type, String title) {
		super();
		this.id = id;
		this.key = key;
		this.value = value;
		this.describe = describe;
		this.type = type;
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
