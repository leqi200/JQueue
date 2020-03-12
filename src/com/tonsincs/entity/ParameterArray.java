package com.tonsincs.entity;

import java.util.List;

/**
* @ProjectName:JQueue
* @ClassName: ParameterArray
* @Description: TODO(用于生成带多个数组参数的ParameterArray)
* @author 萧达光
* @date 2014-5-28 下午01:42:12
* 
* @version V1.0 
*/
public class ParameterArray {
	private List<Parameter> A;
	private List<Parameter> B;
	private List<Parameter> D;
	private List<Parameter> S;
	//private List<Parameter> P;
	
	public ParameterArray(List<Parameter> a, List<Parameter> b,
			List<Parameter> d, List<Parameter> s) {
		super();
		A = a;
		B = b;
		D = d;
		S = s;
		//P=p;
	}
	public List<Parameter> getA() {
		return A;
	}
	public void setA(List<Parameter> a) {
		A = a;
	}
	public List<Parameter> getB() {
		return B;
	}
	public void setB(List<Parameter> b) {
		B = b;
	}
	public List<Parameter> getD() {
		return D;
	}
	public void setD(List<Parameter> d) {
		D = d;
	}
	public List<Parameter> getS() {
		return S;
	}
	public void setS(List<Parameter> s) {
		S = s;
	}
	// public List<Parameter> getP() {
	// return P;
	// }
	// public void setP(List<Parameter> p) {
	// P = p;
	// }
	
	

}
