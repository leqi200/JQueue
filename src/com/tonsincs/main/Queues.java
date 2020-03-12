package com.tonsincs.main;

/**
 * @ProjectName:JQueue
 * @ClassName: Queues
 * @Description: TODO(队列描述类)
 * @author 萧达光
 * @date 2014-5-28 下午01:36:26
 * 
 * @version V1.0
 */
public class Queues {
	private String callNumber; // 排队号码
	private Integer counterNo; // 窗口号
	private String bizNo; // 业务编码
	private String bizName;// 业务名称
	private String action;// 动作名称[call:呼叫、pause:暂停]

	public Queues(String callNumber, Integer counterNo, String bizNo,
			String bizName) {
		this.callNumber = callNumber;
		this.counterNo = counterNo;
		this.bizNo = bizNo;
		this.bizName = bizName;
	}

	public Queues(String callNumber, Integer counterNo, String bizNo,
			String bizName, String action) {
		super();
		this.callNumber = callNumber;
		this.counterNo = counterNo;
		this.bizNo = bizNo;
		this.bizName = bizName;
		this.action = action;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public Integer getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(Integer counterNo) {
		this.counterNo = counterNo;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "Queues [callNumber=" + callNumber + ", counterNo=" + counterNo
				+ ", bizNo=" + bizNo + ", bizName=" + bizName + ", action="
				+ action + "]";
	}

}
