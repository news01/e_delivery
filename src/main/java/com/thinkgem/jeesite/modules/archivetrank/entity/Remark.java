package com.thinkgem.jeesite.modules.archivetrank.entity;
/***
 * 
 * @author zmj
 *	每次签收的备注内容，主要是名称和数量
 */
public class Remark {
	private String name;
	private String num;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
}
